package org.glow.discordBot.action.chest;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.items.Item;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public abstract class Chest implements Oppened {

    private Player player;

    private String name;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Chest(Player player) {
        this.player = player;
    }

    public void coins(SlashCommandInteractionEvent event, int coins) {

        player.setCoins(player.getCoins() + coins);
        SaveAndLoad.INSTANCE.save(player);
        logger.info("{}: ИГРОК С ID {} ПОЛУЧАЕТ {} {}",
                getName().toUpperCase(), player.getId(), coins, Parameters.COINS.getName().toUpperCase());

        String title = String.format("""
                        %s находит %s
                        """,
                PersonWorker.INSTANCE.getPersonName(player), getName());
        String description = String.format("""
                        %s получает %s %s
                                        
                        %s
                        """,
                PersonWorker.INSTANCE.getPersonName(player), coins, Parameters.COINS.getName(),
                TextCreator.INSTANCE.getPlayerParameters(player));
        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description).build().replyMessage();

    }

    public void item(SlashCommandInteractionEvent event, List<Item> items) {

        Item randomItem = items.get(new Random().nextInt(items.size()));
        logger.info("{}: СЛУЧАЙНЫЙ ПРЕДМЕТ: {}", getName().toUpperCase(), randomItem.getName());

        if (player.getInventory().getBag().getItems().size() == player.getInventory().getBag().getSize()) {

            logger.debug("{}: ИНВЕТАРЬ ЗАПОЛНЕН", getName().toUpperCase());

            int coins = randomItem.getPrice() / 2;
            player.setCoins(player.getCoins() + coins);
            SaveAndLoad.INSTANCE.save(player);
            logger.info("{}: ИГРОК С ID {} ПОЛУЧАЕТ {} {}",
                    getName().toUpperCase(), player.getId(), coins, Parameters.COINS.getName().toUpperCase());

            String title = String.format("""
                    %s находит %s
                    """, PersonWorker.INSTANCE.getPersonName(player), getName());
            String description = String.format("""
                    %s открывает сундук и находит в нем %s
                    Но инвентарь заполнен, поэтому ты продаешь %s первому встречному и получаешь %s %s
                    
                    %s
                    """, PersonWorker.INSTANCE.getPersonName(player), randomItem.getName(),
                    randomItem.getName(), coins, Parameters.COINS.getName(),
                    TextCreator.INSTANCE.getPlayerParameters(player));
            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(description).build().replyMessage();

        } else {

            player.getInventory().getBag().addItem(randomItem);
            SaveAndLoad.INSTANCE.save(player);
            logger.info("{}: ИГРОК С ID {} ПОЛУЧАЕТ {}",
                    getName().toUpperCase(), player.getId(), randomItem.getName());

            String title = String.format("""
                    %s находит %s
                    """, PersonWorker.INSTANCE.getPersonName(player), getName());
            String description = String.format("""
                    %s открывает сундук и находит в нем %s
                    
                    %s
                    """, PersonWorker.INSTANCE.getPersonName(player), randomItem.getName(),
                    TextCreator.INSTANCE.getPlayerEquippedItems(player));
            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(description).build().replyMessage();

        }

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
