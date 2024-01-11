package org.glow.discordBot.action.still;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.action.Action;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.items.Item;
import org.glow.discordBot.items.ItemWorker;
import org.glow.discordBot.items.handleft.HandLeftFactory;
import org.glow.discordBot.items.handright.HandRightFactory;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityMondstadtSteal implements Action {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Player player;
    private SlashCommandInteractionEvent event;

    @Override
    public String getName() {
        return "Мондштадт";
    }

    @Override
    public String getDescription() {
        return String.format("""
                Не так много стражи на улицах и довольно невнимательные жители, но и %s у них с собой не так много
                """, Parameters.COINS.getName());
    }

    @Override
    public void start(SlashCommandInteractionEvent event, Player player) {

        this.player = player;
        this.event = event;

        logger.info("{}: СТАРТ",
                getName().toUpperCase());

        player.setEnergy(player.getEnergy() - 1);
        SaveAndLoad.INSTANCE.save(player);
        logger.info("{}: ИГРОК ID: {} ТЕРЯЕТ 1 ЭНЕРГИЮ",
                getName().toUpperCase(), player.getId());

        int random = new Random().nextInt(1, 101);
        logger.debug("{}: СЛУЧАЙНОЕ ЧИСЛО {}",
                getName().toUpperCase(), random);
        int playerStealChance = 20;

        if (playerStealChance >= random) {
            new PlayerStill(event, player).steal();
        } else {
            npcSteal();
        }

    }

    private void npcSteal() {

        logger.debug("{}: УКРАСТЬ У НИП",
                getName().toUpperCase());

        int random = new Random().nextInt(1, 101);
        logger.debug("{}: СЛУЧАЙНОЕ ЧИСЛО {}",
                getName().toUpperCase(), random);
        int stealChance = 50;
        stealChance += player.getAgility().getValue() + player.getPerception().getValue();
        stealChance -= (int) (0.1 * PersonWorker.INSTANCE.getPersonLevel(player));
        logger.debug("{}: ШАНС УКРАСТЬ {}",
                getName().toUpperCase(), stealChance);

        if (stealChance >= random) {
            steal();
        } else {
            notSteal();
        }

    }

    private void steal() {

        logger.debug("{}: УСПЕШНО КРАДЕТ",
                getName().toUpperCase());

        int random = new Random().nextInt(1, 101);
        logger.debug("{}: СЛУЧАЙНОЕ ЧИСЛО {}",
                getName().toUpperCase(), random);
        int itemChance = 5;

        if (itemChance >= random) {
            stealItem();
        } else {
            stealMora();
        }

    }

    private void stealMora() {

        logger.debug("{}: КРАДЕТ {}",
                getName().toUpperCase(), Parameters.COINS.getName().toUpperCase());

        int coins = new Random().nextInt(1, 6);
        coins += player.getLuck().getValue() + player.getLuck().getValue();
        coins *= 10;

        player.setCoins(player.getCoins() + coins);
        SaveAndLoad.INSTANCE.save(player);
        logger.info("{}: ИГРОК ID: {}, ПОЛУЧИЛ {} {}",
                getName().toUpperCase(), player.getId(), coins, Parameters.COINS.getName().toUpperCase());

        String title = String.format("""
                %s успешно крадет %s
                """, PersonWorker.INSTANCE.getPersonName(player), Parameters.COINS.getName());
        String description = String.format("""
                %s украл %s %s у случайного прохожего
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(player), coins, Parameters.COINS.getName(),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

    private void stealItem() {

        logger.debug("{}: КРАДЕТ ПРЕДМЕТ",
                getName().toUpperCase());

        List<Item> items = new ArrayList<>(ItemWorker.INSTANCE.getIronArmor());
        items.add(HandRightFactory.INSTANCE.getDullBlade());
        items.add(HandLeftFactory.INSTANCE.getIronShield());

        Item item = items.get(new Random().nextInt(items.size()));
        logger.debug("{}: СЛУЧАЙНЫЙ ПРЕДМЕТ: {}",
                getName().toUpperCase(), item.getName().toUpperCase());

        if (player.getInventory().getBag().getItems().size() == player.getInventory().getBag().getSize()) {

            logger.debug("{}: ИНВЕТАРЬ ЗАПОЛНЕН",
                    getName().toUpperCase());

            int coins = item.getPrice() / 2;
            player.setCoins(player.getCoins() + coins);
            SaveAndLoad.INSTANCE.save(player);
            logger.info("{}: ИГРОК ID: {} ПОЛУЧИЛ {} {}",
                    getName().toUpperCase(), player.getId(), coins, Parameters.COINS.getName().toUpperCase());

            String title = String.format("""
                    %s кадет %s
                    """, PersonWorker.INSTANCE.getPersonName(player), getName());
            String description = String.format("""
                    %s врует из магазина %s
                    Но инвентарь заполнен, поэтому ты продаешь %s первому встречному и получаешь %s %s
                    
                    %s
                    """, PersonWorker.INSTANCE.getPersonName(player), item.getName(),
                    item.getName(), coins, Parameters.COINS.getName(),
                    TextCreator.INSTANCE.getPlayerParameters(player));

            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(description)
                    .build().replyMessage();

        } else {

            player.getInventory().getBag().addItem(item);
            SaveAndLoad.INSTANCE.save(player);
            logger.info("{}: ИГРОК ID: {} ПОЛУЧИЛ {}",
                    getName().toUpperCase(), player.getId(), item.getName());

            String title = String.format("""
                    %s урал %s
                    """, PersonWorker.INSTANCE.getPersonName(player), getName());
            String description = String.format("""
                    %s ворует из магазина %s
                    
                    %s
                    """, PersonWorker.INSTANCE.getPersonName(player), item.getName(),
                    TextCreator.INSTANCE.getPlayerEquippedItems(player));

            new MessageSender.MessageBuilder(event.getHook(), title)
                    .setDescription(description)
                    .build().replyMessage();

        }

    }

    private void notSteal() {

        logger.debug("{}: НЕ УДАЛОСЬ УКРАСТЬ",
                getName().toUpperCase());

        int random = new Random().nextInt(1, 101);
        logger.debug("{}: СЛУЧАЙНОЕ ЧИСЛО {}",
                getName().toUpperCase(), random);
        int escapeChance = 50;
        escapeChance += player.getEndurance().getValue();
        escapeChance -= PersonWorker.INSTANCE.getPersonLevel(player);
        logger.debug("{}: ШАНС УКРАСТЬ {}",
                getName().toUpperCase(), escapeChance);

        if (escapeChance >= random) {
            escape();
        } else {
            notEscape();
        }

    }

    private void escape() {

        logger.info("{}: НЕЗАМЕЧЕН", getName().toUpperCase());

        String title = String.format("""
                %s не удалось украсть
                """, PersonWorker.INSTANCE.getPersonName(player));
        String description = String.format("""
                %s не удалось украсть, но никто ничего не заметил
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(player),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

    private void notEscape() {

        logger.info("{}: ЗАМЕЧЕН",
                getName().toUpperCase());

        player.setEnergy(player.getEnergy() - 3);
        SaveAndLoad.INSTANCE.save(player);

        logger.info("{}: ИГРОК С ID {} ТЕРЯЕТ 3 ЭНЕРГИИ",
                getName().toUpperCase(), player.getId());

        String title = String.format("""
                %s не удалось украсть
                """, PersonWorker.INSTANCE.getPersonName(player));
        String description = String.format("""
                %s заметили, он очень долго убегал от стражи и сильно устал
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(player),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

}
