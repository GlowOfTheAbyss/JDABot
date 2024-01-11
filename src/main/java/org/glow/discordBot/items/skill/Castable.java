package org.glow.discordBot.items.skill;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.Player;

public interface Castable {

    void cast(SlashCommandInteractionEvent event, Player player);

}
