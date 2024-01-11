package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.TextCreator;

public class ShowPlayerInventoryCommand implements SlashCommand {

    @Override
    public String getName() {
        return "inventory";
    }

    @Override
    public String getDescription() {
        return "показать инвентарь";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        String title = "Инвентарь " + PersonWorker.INSTANCE.getPersonName(player);

        String description = String.format("""
                %s
                %s
                """, TextCreator.INSTANCE.getPlayerEquippedItems(player),
                TextCreator.INSTANCE.getPlayerInventory(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash(getName(), getDescription());
    }

}
