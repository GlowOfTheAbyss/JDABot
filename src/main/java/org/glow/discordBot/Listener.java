package org.glow.discordBot;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.glow.discordBot.commands.CommandLogger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listener extends ListenerAdapter {

    private CommandLogger commandLogger;
    private final ExecutorService executorService;

    public Listener() {
        // создаем менеджер потоков на 4 потока
        executorService = Executors.newFixedThreadPool(4, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

    // ловис команды доступные по слешу
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        commandLogger.getSlashCommands().forEach(command -> {
            if (command.getName().equals(event.getName())) {
                executorService.submit(() -> command.start( event));
            }
        });

    }

    // ловим команды с автозаполнением
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {

        commandLogger.getAutoCompleteCommands().forEach(command -> {
            if (command.getName().equals(event.getName())) {
                executorService.submit(() -> command.autoComplete(event));
            }
        });

    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        this.commandLogger = new CommandLogger(event.getGuild());

    }
}
