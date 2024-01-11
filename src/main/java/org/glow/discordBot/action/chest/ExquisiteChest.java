package org.glow.discordBot.action.chest;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.items.Item;
import org.glow.discordBot.items.ItemWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExquisiteChest extends Chest {

    public ExquisiteChest(Player player) {
        super(player);
        setName("Богатый сундук");
    }

    @Override
    public void open(SlashCommandInteractionEvent event) {

        logger.info("{}: СТАРТ", getName().toUpperCase());

        int random = new Random().nextInt(1, 101);
        logger.info("{}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int itemChance = 5;
        itemChance += (int) (0.05 * getPlayer().getLuck().getValue());
        logger.info("{}: ШАНС ПРЕДМЕТА {}", getName().toUpperCase(), itemChance);

        if (itemChance >= random) {

            List<Item> items = new ArrayList<>();
            items.addAll(ItemWorker.INSTANCE.getIronArmor());
            items.addAll(ItemWorker.INSTANCE.getWhiteIronArmor());
            items.addAll(ItemWorker.INSTANCE.getWeapons());
            items.addAll(ItemWorker.INSTANCE.getShields());

            item(event, items);

        } else {
            int coins = new Random().nextInt(10, 16);
            coins += getPlayer().getLuck().getValue();
            coins *= 10;

            coins(event, coins);
        }

    }



}
