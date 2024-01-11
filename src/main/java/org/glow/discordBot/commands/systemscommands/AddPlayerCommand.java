package org.glow.discordBot.commands.systemscommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddPlayerCommand implements SlashCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Добавляет нового игрока";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        logger.debug("КОММАНДА {}: СТАРТ", getName().toUpperCase());

        event.deferReply().queue();

        for (Player player : PersonWorker.INSTANCE.getPlayers()) {
            if (player.getId().equals(event.getUser().getId())) {

                logger.info("КОММАНДА {}: ИГРОК С ID {} УЖЕ СУЩЕСТВУЕТ", getName().toUpperCase(), player.getId());

                String title = String.format("""
                        %s уже существует
                        """, PersonWorker.INSTANCE.getPersonName(player));
                new MessageSender.MessageBuilder(event.getHook(), title)
                        .build()
                        .replyMessage();
                return;
            }
        }

        // создание нового игрока
        Player player = new Player(event.getUser().getId());
        logger.info("КОММАНДА {}: ИГРОК С ID {} СОЗДАН", getName().toUpperCase(), player.getId());
        PersonWorker.INSTANCE.addPlayer(player);
        SaveAndLoad.INSTANCE.save(player);

        String title = String.format("""
                игрок %s успешно создан
                """, PersonWorker.INSTANCE.getPersonName(player));
        String description = TextCreator.INSTANCE.getPlayerParameters(player);
        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description).build().replyMessage();

    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash(getName(), getDescription());
    }

}
