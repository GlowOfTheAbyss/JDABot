package org.glow.discordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface SlashCommand extends Command {

    void start(SlashCommandInteractionEvent event);

    SlashCommandData getSlashCommandData();

}
