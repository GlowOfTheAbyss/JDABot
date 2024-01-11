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
import org.glow.discordBot.items.*;
import org.glow.discordBot.message.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class EquipItemCommand implements SlashCommand, AutoCompleteCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String optionName = "item";

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        List<Item> items = player.getInventory().getBag().getItems();

        // сравниваем выбраную опцию пользователем с имеющейся
        if (event.getFocusedOption().getName().equals(optionName)) {
            // добавляем в варианты выбора все вещи из инвентаря игрока
            List<Command.Choice> option = items.stream()
                    .map(item -> new Command.Choice(item.getName(), item.getName()))
                    .toList();
            event.replyChoices(option).queue();
        }

    }

    @Override
    public String getName() {
        return "equip";
    }

    @Override
    public String getDescription() {
        return "экипировать предмет из сумки";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        logger.debug("КОММАНДА {}: СТАРТ", getName().toUpperCase());

        event.deferReply().queue();

        // ищем игрока по ID и проверяем сражается ли он
        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        if (FightWorker.INSTANCE.personInFight(player)) {

            logger.info("КОММАНДА {}: ИГРОК {} В БОЮ",
                    getName().toUpperCase(),
                    PersonWorker.INSTANCE.getPersonName(player).toUpperCase());

            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }

        // получаем опцию выбранную игроком в виде строчки
        String itemName = Objects.requireNonNull(event.getOption(optionName)).getAsString();
        Item item = ItemWorker.INSTANCE.findItemByName(itemName);
        if (item instanceof Equipped) {
            ((Equipped) item).equip(player.getInventory());

            logger.info("КОММАНДА {}: ПРЕДМЕТ {} ЭКИПЕРОВАН", getName().toUpperCase(), item.getName().toUpperCase());

            SaveAndLoad.INSTANCE.save(player);
            String title = String.format("""
                %s экиперован
                """, item.getName());

            new MessageSender.MessageBuilder(event.getHook(), title)
                    .build().replyMessage();

        } else {

            logger.info("КОММАНДА {}: ПРЕДМЕТ {} НЕВОЗМОЖНО ЭКИПИРОВАТЬ", getName().toUpperCase(),
                    item.getName().toUpperCase());

            new MessageSender.MessageBuilder(event.getHook(), "Предмет нельзя экипировать")
                    .build().replyMessage();

        }

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
