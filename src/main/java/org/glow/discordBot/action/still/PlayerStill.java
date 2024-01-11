package org.glow.discordBot.action.still;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerStill {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Player player;
    private final SlashCommandInteractionEvent event;

    public PlayerStill(SlashCommandInteractionEvent event, Player player) {
        this.player = player;
        this.event = event;
    }

    public void steal() {

        logger.info("{}: СТАРТ", this.getClass().getSimpleName().toUpperCase());

        List<Player> people = new ArrayList<>(PersonWorker.INSTANCE.getPlayers());
        Player victim = people.get(new Random().nextInt(people.size()));

        int stealValue = new Random().nextInt(1, 21);
        stealValue += 2 * player.getAgility().getValue();
        stealValue += player.getIntelligence().getValue();
        stealValue += (int) (0.5 * player.getLuck().getValue());
        logger.debug("{}: УКРАСТЬ: {}", this.getClass().getSimpleName().toUpperCase(), stealValue);

        int victimValue = new Random().nextInt(1, 21);
        victimValue += 2 * victim.getPerception().getValue();
        victimValue += victim.getIntelligence().getValue();
        victimValue += (int) (0.5 * victim.getLuck().getValue());
        logger.debug("{}: ЗАЩИТА ОТ ВОРОВСТВА: {} ", this.getClass().getSimpleName().toUpperCase(), victimValue);


        String title;
        String description;

        if (stealValue > victimValue) {

            if (stealValue > victimValue * 2) {

                logger.debug("{}: СУПЕР КРАЖА", this.getClass().getSimpleName().toUpperCase());

                int coins = new Random().nextInt(5, 11);
                coins += 4 * player.getAgility().getValue();
                coins += player.getLuck().getValue();
                coins -= 2 + victim.getIntelligence().getValue();
                if (victim.getCoins() < coins) {
                    coins = victim.getCoins();
                }

                victim.setCoins(victim.getCoins() - coins);
                player.setCoins(player.getCoins() + coins);
                SaveAndLoad.INSTANCE.save(victim);
                SaveAndLoad.INSTANCE.save(player);

                logger.info("{}: ИГРОК ID: {} УКРАЛ У ИГРОКА ID: {} {} {}",
                        this.getClass().getSimpleName().toUpperCase(), player.getId(), victim.getId(),
                        coins, Parameters.COINS.getName().toUpperCase());

                title = String.format("""
                        %s совершил супер кражу
                        """, PersonWorker.INSTANCE.getPersonName(player));
                description = String.format("""
                        %s украл у %s %s %s
                        
                        %s
                        """, PersonWorker.INSTANCE.getPersonName(player), PersonWorker.INSTANCE.getPersonName(victim),
                        coins, Parameters.COINS.getName(), TextCreator.INSTANCE.getPlayerParameters(player));

            } else {

                logger.debug("{}: КРАЖА", this.getClass().getSimpleName().toUpperCase());

                int coins = new Random().nextInt(1, 6);
                coins += 2 * player.getAgility().getValue();
                coins += player.getLuck().getValue();
                coins -= victim.getIntelligence().getValue();
                if (victim.getCoins() < coins) {
                    coins = victim.getCoins();
                }

                victim.setCoins(victim.getCoins() - coins);
                player.setCoins(player.getCoins() + coins);
                SaveAndLoad.INSTANCE.save(victim);
                SaveAndLoad.INSTANCE.save(player);

                logger.info("{}: ИГРОК ID: {} УКРАЛ У ИГРОКА ID: {} {} {}",
                        this.getClass().getSimpleName().toUpperCase(), player.getId(), victim.getId(),
                        coins, Parameters.COINS.getName().toUpperCase());

                title = String.format("""
                        %s совершил кражу
                        """, PersonWorker.INSTANCE.getPersonName(player));
                description = String.format("""
                        %s украл у %s %s %s
                        
                        %s
                        """, PersonWorker.INSTANCE.getPersonName(player), PersonWorker.INSTANCE.getPersonName(victim),
                        coins, Parameters.COINS.getName(), TextCreator.INSTANCE.getPlayerParameters(player));

            }

        } else {

            logger.debug("{}: КРАЖА НЕ УДАЛАСЬ", this.getClass().getSimpleName().toUpperCase());

            if (victimValue > stealValue * 2) {

                logger.debug("{}: ИГРОК ID: {} ЗАМЕЧЕН",
                        this.getClass().getSimpleName().toUpperCase(), player.getId());

                player.setEnergy(player.getEnergy() - 3);
                SaveAndLoad.INSTANCE.save(player);
                logger.info("{}: ИГРОК ID: {} ТЕРЯЕТ 3 ЭНЕРГИИ", this.getClass().getSimpleName().toUpperCase(),
                        player.getId());

                title = String.format("""
                        %s не удалось совершить кражу
                        """, PersonWorker.INSTANCE.getPersonName(player));
                description = String.format("""
                        %s не удалось украть у %s, вас заметили и вам пришлось очень долго бежать от стражи
                        
                        %s
                        """, PersonWorker.INSTANCE.getPersonName(player), PersonWorker.INSTANCE.getPersonName(victim),
                        TextCreator.INSTANCE.getPlayerParameters(player));

            } else {

                logger.debug("{}: ИГРОК ID: {} НЕЗАМЕЧЕН",
                        this.getClass().getSimpleName().toUpperCase(), player.getId());

                title = String.format("""
                        %s не удалось совершить кражу
                        """, PersonWorker.INSTANCE.getPersonName(player));
                description = String.format("""
                        %s не удалось украть у %s, но вашу попытку никто не заметил
                        
                        %s
                        """, PersonWorker.INSTANCE.getPersonName(player), PersonWorker.INSTANCE.getPersonName(victim),
                        TextCreator.INSTANCE.getPlayerParameters(player));

            }

        }

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

}
