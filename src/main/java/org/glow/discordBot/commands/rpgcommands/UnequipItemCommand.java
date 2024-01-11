package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.commands.AutoCompleteCommand;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.items.Item;
import org.glow.discordBot.items.ItemWorker;
import org.glow.discordBot.items.Unequipped;
import org.glow.discordBot.message.MessageSender;

import java.util.List;
import java.util.Objects;

public class UnequipItemCommand implements SlashCommand, AutoCompleteCommand {

    private final String optionName = "item";

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        List<Item> items = ItemWorker.INSTANCE.getPlayerEquippedItems(player);

        if (event.getFocusedOption().getName().equals(optionName)) {
            List<Command.Choice> option = items.stream()
                    .filter(item -> !item.getName().equals("Пусто"))
                    .map(item -> new Command.Choice(item.getName(), item.getName()))
                    .toList();
            event.replyChoices(option).queue();
        }

    }

    @Override
    public String getName() {
        return "unequip";
    }

    @Override
    public String getDescription() {
        return "снять с себя предмет";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());

        if (FightWorker.INSTANCE.personInFight(player)) {
            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }

        if (player.getInventory().getBag().getItems().size() == player.getInventory().getBag().getSize()) {
            new MessageSender.MessageBuilder(event.getHook(), "инвентарь заполнен")
                    .build().replyMessage();
            return;
        }

        String itemName = Objects.requireNonNull(event.getOption(optionName)).getAsString();
        Item item = ItemWorker.INSTANCE.findItemByName(itemName);
        if (item instanceof Unequipped) {
            ((Unequipped) item).unequip(player.getInventory());

            SaveAndLoad.INSTANCE.save(player);
            String title = String.format("""
                %s снят
                """, item.getName());

            new MessageSender.MessageBuilder(event.getHook(), title)
                    .build().replyMessage();

        } else {

            new MessageSender.MessageBuilder(event.getHook(), "Предмет нельзя снять")
                    .build().replyMessage();

        }

    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData optionData = new OptionData(OptionType.STRING, optionName, "снимаемый предмет",
                true, true);
        data.addOptions(optionData);

        return data;
    }
}
