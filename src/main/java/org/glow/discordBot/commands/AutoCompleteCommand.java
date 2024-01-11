package org.glow.discordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

public interface AutoCompleteCommand extends Command {

    void autoComplete(CommandAutoCompleteInteractionEvent event);

}
