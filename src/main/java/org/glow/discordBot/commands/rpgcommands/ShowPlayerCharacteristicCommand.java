package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.message.TextCreator;

public class ShowPlayerCharacteristicCommand implements SlashCommand {

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "показать характеристики";
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());

        String title = PersonWorker.INSTANCE.getPersonName(player);

        String description = String.format("""  
                        %s
                        Уровень: %s
                        Боевой уровень: %s
                                            
                        %s
                        """,
                TextCreator.INSTANCE.getPlayerParameters(player),
                PersonWorker.INSTANCE.getPersonLevel(player),
                PersonWorker.INSTANCE.getPersonCombatLevel(player),
                TextCreator.INSTANCE.getPlayerCharacteristic(player)
        );

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description);
        event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public SlashCommandData getSlashCommandData() {
        return Commands.slash(getName(), getDescription());
    }

}
