package org.glow.discordBot.action.chest;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CommonChest extends Chest{

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommonChest(Player player) {
        super(player);
        setName("Обычный сундук");
    }

    @Override
    public void open(SlashCommandInteractionEvent event) {

        logger.info("{}: СТАРТ", getName().toUpperCase());

        int coins = new Random().nextInt(5, 11);
        coins += getPlayer().getLuck().getValue();
        coins *= 10;

        coins(event, coins);

    }

}
