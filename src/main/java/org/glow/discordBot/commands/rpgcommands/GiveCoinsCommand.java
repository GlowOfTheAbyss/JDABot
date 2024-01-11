package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;

import java.util.Objects;

public class GiveCoinsCommand implements SlashCommand {

    private final String playerOptionName = "player";
    private final String valueOptionName = "value";

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return String.format("передать %s другому игроку ", Parameters.COINS.getName().toLowerCase());
    }

    @Override
    public void start(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        Player player = PersonWorker.INSTANCE.getPlayerById(event.getUser().getId());
        if (FightWorker.INSTANCE.personInFight(player)) {
            new MessageSender.MessageBuilder(event.getHook(), "Нельзя это использовать в бою")
                    .build().replyMessage();
            return;
        }

        Player targetPlayer = PersonWorker.INSTANCE.getPlayerById(
                Objects.requireNonNull(event.getOption(playerOptionName)).getAsUser().getId());

        int value = Objects.requireNonNull(event.getOption(valueOptionName)).getAsInt();
        if (value <= 0) {
            new MessageSender.MessageBuilder(event.getHook(), "Неккоректное число")
                    .build().replyMessage();
            return;
        }
        if (player.getCoins() < value) {
            new MessageSender.MessageBuilder(event.getHook(), "Недостаточно "
                    + Parameters.COINS.getName() + " на счету")
                    .build().replyMessage();
            return;
        }

        player.setCoins(player.getCoins() - value);
        targetPlayer.setCoins(targetPlayer.getCoins() + value);
        SaveAndLoad.INSTANCE.save(player);
        SaveAndLoad.INSTANCE.save(targetPlayer);

        String title = String.format("""
                %s переводит %s %s %s
                """, PersonWorker.INSTANCE.getPersonName(player), value, Parameters.COINS.getName(),
                PersonWorker.INSTANCE.getPersonName(targetPlayer));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(TextCreator.INSTANCE.getPlayerParameters(player))
                .build().replyMessage();

    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData playerOption = new OptionData(OptionType.USER, playerOptionName, "выберите игрока",
                true);
        OptionData valueOption = new OptionData(OptionType.INTEGER, valueOptionName, "выберите сумму",
                true);

        data.addOptions(playerOption, valueOption);
        return data;
    }
}
