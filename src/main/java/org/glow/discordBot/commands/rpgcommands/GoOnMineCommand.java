package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.action.Action;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.action.mine.MtTianhengMine;
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

public class GoOnMineCommand implements SlashCommand {

    private final String optionName = "location";
    private final List<Action> mineAction = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GoOnMineCommand() {
        mineAction.add(new MtTianhengMine());
    }

    @Override
    public String getName() {
        return "mine";
    }

    @Override
    public String getDescription() {
        return "отправиться в шахту на выбор";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        logger.info("КОММАНДА {}: СТАРТ", getName().toUpperCase());

        event.deferReply().queue();

        if (isNull(event.getOption(optionName))) {
            logger.info("КОММАНДА {}: ОПЦИИ {} НЕ ОБНАРУЖЕНО", getName().toUpperCase(), optionName.toUpperCase());
            showInfo(event);
            return;
        }

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        logger.debug("КОММАНДА {}: ИГРОК С ID: {} ПОЛУЧЕН", getName().toUpperCase(), player.getId());
        if (FightWorker.INSTANCE.personInFight(player)) {
            logger.info("КОММАНДА {}: ИГРОК С ID: {} В БОЮ", getName().toUpperCase(), player.getId());
            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }
        if (player.getEnergy() <= 0) {
            logger.info("КОММАНДА {}: У ИГРОК С ID: {} НЕДОСТАТОЧНО ЭНЕРГИИ", getName().toUpperCase(), player.getId());
            new MessageSender.MessageBuilder(event.getHook(), "недостаточно энергии")
                    .build().replyMessage();
            return;
        }

        logger.debug("КОММАНДА {}: ПОИСК ИМЕНИ АКТИВНОСТИ ПО ПАРАМЕТРУ: {}",
                getName().toUpperCase(), optionName.toUpperCase());
        String actionName = Objects.requireNonNull(event.getOption(optionName)).getAsString();
        logger.debug("КОММАНДА {}: ИМЯ АКТИВНОСТИ ПОЛУЧЕНО: {}", getName().toUpperCase(), actionName.toUpperCase());

        mineAction.forEach(action -> {
            if (actionName.equals(action.getName())) {
                logger.info("КОММАНДА {}: АКТИВНОСТЬ {} НАЙДЕНА, ЗАПУСК",
                        getName().toUpperCase(), action.getName().toUpperCase());
                action.start(event, player);
            }
        });

    }

    private void showInfo(SlashCommandInteractionEvent event) {
        logger.info("КОММАНДА {}: ВЫВОДИМ INFO", getName().toUpperCase());
        StringBuilder stringBuilder = new StringBuilder();
        mineAction.forEach(action -> stringBuilder.append(String.format("""
                **%s** | %s
                """,
                action.getName(), action.getDescription())));
        new MessageSender.MessageBuilder(event.getHook(), "Шахты")
                .setDescription(stringBuilder.toString())
                .build().replyMessage();
    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData optionData = new OptionData(OptionType.STRING, optionName, "место куда отправиться копать");
        mineAction.forEach(action -> optionData.addChoice(action.getName(), action.getName()));

        data.addOptions(optionData);
        return data;
    }
}
