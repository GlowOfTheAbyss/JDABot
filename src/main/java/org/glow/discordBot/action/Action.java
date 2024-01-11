package org.glow.discordBot.action;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.Player;

public interface Action {

    String getName();
    String getDescription();
    void start(SlashCommandInteractionEvent event, Player player);

}
