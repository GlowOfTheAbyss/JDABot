package org.glow.discordBot.commands.rpgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.glow.discordBot.action.casino.Color;
import org.glow.discordBot.action.fight.FightWorker;
import org.glow.discordBot.commands.SlashCommand;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlayInCasinoCommand implements SlashCommand {

    private final String optionColorName = "color";
    private final  String optionValueName = "value";
    private final List<Color> colors;

    public PlayInCasinoCommand() {
        colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.ZERO);
    }

    @Override
    public String getName() {
        return "bet";
    }

    @Override
    public String getDescription() {
        return "сделать ставку в рулетке";
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

        int value = Objects.requireNonNull(event.getOption(optionValueName)).getAsInt();
        if (value <= 0) {
            new MessageSender.MessageBuilder(event.getHook(), "Неккоректная ставка")
                    .build().replyMessage();
            return;
        }
        if (player.getCoins() < value) {
            new MessageSender.MessageBuilder(event.getHook(), "Недостаточно " + Parameters.COINS.getName())
                    .build().replyMessage();
            return;
        }
        player.setCoins(player.getCoins() - value);

        String colorName = Objects.requireNonNull(event.getOption(optionColorName)).getAsString();
        Color color = null;
        for (Color colorFor : colors) {
            if (colorFor.getName().equals(colorName)) {
                color = colorFor;
            }
        }

        int random = new Random().nextInt(0, 37);
        Color winColor = Color.getColorByInt(random);

        assert color != null;
        String title = String.format("""
                    %s ставит на %s
                    """, PersonWorker.INSTANCE.getPersonName(player), color.getName());
        String description;
        if (color.equals(winColor)) {

            value *= 2;
            player.setCoins(player.getCoins() + value);

            description = String.format("""
                    Выпадает: %s %s
                    Выйгрыш: %s
                    
                    %s
                    """, random, winColor.getName(),
                    value,
                    TextCreator.INSTANCE.getPlayerParameters(player));

        } else {

            description = String.format("""
                    Выпадает: %s %s
                    Проигрыш: %s
                    
                    %s
                    """, random, winColor.getName(),
                    value,
                    TextCreator.INSTANCE.getPlayerParameters(player));

        }

        SaveAndLoad.INSTANCE.save(player);

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description).build().replyMessage();

    }

    @Override
    public SlashCommandData getSlashCommandData() {

        SlashCommandData data = Commands.slash(getName(), getDescription());
        OptionData colorOption = new OptionData(OptionType.STRING, optionColorName, "выберите цвет",
                true);
        colors.forEach(color -> colorOption.addChoice(color.getName(), color.getName()));
        OptionData valueOption = new OptionData(OptionType.INTEGER, optionValueName, "сумма ставки",
                true);

        data.addOptions(colorOption, valueOption);
        return data;
    }
}
