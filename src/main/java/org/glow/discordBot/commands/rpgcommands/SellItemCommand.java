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
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;

import java.util.List;
import java.util.Objects;

public class SellItemCommand implements SlashCommand, AutoCompleteCommand {

    private final String optionName = "item";

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        List<Item> items = player.getInventory().getBag().getItems();

        if (event.getFocusedOption().getName().equals(optionName)) {
            List<Command.Choice> option = items.stream()
                    .map(item -> new Command.Choice(item.getName(), item.getName()))
                    .toList();
            event.replyChoices(option).queue();
        }

    }

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public String getDescription() {
        return "продать предмет из сумки";
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
        String itemName = Objects.requireNonNull(event.getOption(optionName)).getAsString();
        Item item = ItemWorker.INSTANCE.findItemByName(itemName);

        player.setCoins(player.getCoins() + (item.getPrice() / 2));
        player.getInventory().getBag().removeItem(item);
        SaveAndLoad.INSTANCE.save(player);

        String title = String.format("""
                %s продан
                """, item.getName());
        String description = String.format("""
                За его продажу было получено %s %s
                
                %s
                """, item.getPrice() / 2, Parameters.COINS.getName(),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData optionData = new OptionData(OptionType.STRING, optionName, "выберите предмет",
                true, true);
        data.addOptions(optionData);

        return data;
    }
}
