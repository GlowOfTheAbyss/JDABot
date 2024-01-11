package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.action.Action;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.action.still.CityMondstadtSteal;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.message.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

public class PlayerStealCommand implements SlashCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String optionName = "location";
    private final List<Action> stealActions;

    public PlayerStealCommand() {
        stealActions = new ArrayList<>();
        stealActions.add(new CityMondstadtSteal());
    }

    @Override
    public String getName() {
        return "steal";
    }

    @Override
    public String getDescription() {
        return "украсть что нибудь ценное у случайного нпс или с небольшим шансом у случайного игрока";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        logger.info("{}: СТАРТ",
                getName().toUpperCase());

        event.deferReply().queue();

        if (isNull(event.getOption(optionName))) {
            logger.debug("{}: ОПЦИИЯ: {} НЕ НАЙДЕНА",
                    getName().toUpperCase(), optionName.toUpperCase());

            showInfo(event);
            return;
        }

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        logger.debug("{}: ИГРОК ID: {} НАЙДЕН",
                getName().toUpperCase(), player.getId());

        if (FightWorker.INSTANCE.personInFight(player)) {
            logger.info("{}: ИГРОК ID: {}, В БОЮ",
                    getName().toUpperCase(), player.getId());

            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }
        if (player.getEnergy() <= 0) {
            logger.info("{}: ИГРОК ID: {}, НЕДОСТАТОЧНО ЭНЕРГИИ",
                    getName().toUpperCase(), player.getId());

            new MessageSender.MessageBuilder(event.getHook(), "Недостаточно энергии")
                    .build().replyMessage();
            return;
        }

        String actionName = Objects.requireNonNull(event.getOption(optionName)).getAsString();
        stealActions.forEach(action -> {
            if (actionName.equals(action.getName())) {
                logger.info("{}: НАЙДЕНО {}, ЗАПУСК",
                        getName().toUpperCase(), action.getName().toUpperCase());

                action.start(event, player);
            }
        });

    }

    private void showInfo(SlashCommandInteractionEvent event) {
        logger.info("{}: ВЫВОДИМ INFO",
                getName().toUpperCase());

        StringBuilder stringBuilder = new StringBuilder();
        stealActions.forEach(action -> stringBuilder.append(String.format("""
                **%s** | %s
                """,
                action.getName(), action.getDescription())));

        new MessageSender.MessageBuilder(event.getHook(), "Города")
                .setDescription(stringBuilder.toString())
                .build().replyMessage();
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData optionData = new OptionData(OptionType.STRING, optionName, "выберите локацию для кражи");
        stealActions.forEach(action -> optionData.addChoice(action.getName(), action.getName()));

        data.addOptions(optionData);
        return data;
    }
}
