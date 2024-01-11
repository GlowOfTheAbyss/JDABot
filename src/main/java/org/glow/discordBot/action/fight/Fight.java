package org.glow.discordBot.action.fight;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.InventoryWorker;
import org.glow.discordBot.entities.Person;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.entities.npc.NPC;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Fight {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Person attacker;
    private final Person defender;
    private final MessageChannel messageChannel;

    public Fight(Person attacker, Person defender, SlashCommandInteractionEvent event) {
        this.attacker = attacker;
        this.defender = defender;
        messageChannel = event.getMessageChannel();
    }

    public void start() {

        while (true) {

            attack(attacker, defender);
            if (attacker.getHealth() <= 0) {
                win(defender, attacker);
                break;
            }
            if (defender.getHealth() <= 0) {
                win(attacker, defender);
                break;
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException exception) {
                throw new IllegalArgumentException("Ошибка во время битвы");
            }

            attack(defender, attacker);
            if (attacker.getHealth() <= 0) {
                win(defender, attacker);
                break;
            }
            if (defender.getHealth() <= 0) {
                win(attacker, defender);
                break;
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException exception) {
                throw new IllegalArgumentException("Ошибка во время битвы");
            }

        }

    }

    private void attack(Person attack, Person defend) {

        logger.debug("{}: АТАКА",
                this.getClass().getSimpleName().toUpperCase());

        int attackValue = new Random().nextInt(1, 21);
        attackValue += 2 * attack.getPerception().getValue();
        attackValue += (int) (0.2 * attack.getLuck().getValue());
        logger.debug("{}: АТАКА: {}",
                this.getClass().getSimpleName().toUpperCase(), attackValue);

        int defendValue = new Random().nextInt(1, 21);
        defendValue += 2 * defend.getAgility().getValue();
        defendValue += (int) (0.2 * defend.getLuck().getValue());
        logger.debug("{}: ЗАЩИТА: {}",
                this.getClass().getSimpleName().toUpperCase(), defendValue);

        String title;
        String description;

        if (attackValue > defendValue) {

            int damage;
            if (attackValue > defendValue * 2) {
                logger.debug("{}: КРИТИЧЕСКОЕ ПОПАДАНИЕ",
                        this.getClass().getSimpleName().toUpperCase());

                damage = damage(attack, defend, true);
                title = String.format("""
                        %s наносит критический удар
                        """, PersonWorker.INSTANCE.getPersonName(attack));

            } else {
                logger.debug("{}: ПОПАДАНИЕ",
                        this.getClass().getSimpleName().toUpperCase());

                damage = damage(attack, defend, false);
                title = String.format("""
                        %s наносит удар
                        """, PersonWorker.INSTANCE.getPersonName(attack));

            }

            description = String.format("""
                    %s получает %s урона
                    
                    %s
                    """, PersonWorker.INSTANCE.getPersonName(defend), damage,
                    TextCreator.INSTANCE.getBattleParameters(attacker, defender));

        } else {

            if (defendValue > attackValue * 2) {
                logger.debug("{}: ПАРИРОВАНИЕ",
                        this.getClass().getSimpleName().toUpperCase());

                int damage = damage(defend, attack, false);
                title = String.format("""
                        %s парирует атаку и контр атакует %s
                        """, PersonWorker.INSTANCE.getPersonName(defend), PersonWorker.INSTANCE.getPersonName(attack));
                description = String.format("""
                    %s получает %s урона
                    
                    %s
                    """, PersonWorker.INSTANCE.getPersonName(attack), damage,
                        TextCreator.INSTANCE.getBattleParameters(attacker, defender));

            } else {
                logger.debug("{}: УВОРОТ",
                        this.getClass().getSimpleName().toUpperCase());

                title = String.format("""
                        %s уворачивается от атаки %s
                        """, PersonWorker.INSTANCE.getPersonName(defend), PersonWorker.INSTANCE.getPersonName(attack));
                description = String.format("""
                    %s
                    """, TextCreator.INSTANCE.getBattleParameters(attacker, defender));

            }
        }

        new MessageSender.MessageBuilder(messageChannel, title)
                .setDescription(description)
                .build().replyMessage();

    }

    private int damage(Person dealingDamage, Person takingDamage, boolean crit) {

        int damage = new Random().nextInt(1, 11);
        damage += 2 * dealingDamage.getStrength().getValue();
        damage += (int) (0.2 * dealingDamage.getLuck().getValue());
        damage += InventoryWorker.INSTANCE.getAttack(dealingDamage.getInventory());

        if (crit) {
            damage *= 1.5;
        }

        int defend = new Random().nextInt(0, 7);
        defend += InventoryWorker.INSTANCE.getArmor(takingDamage.getInventory());

        damage -= defend;
        if (damage <= 0) {
            damage = 1;
        }

        logger.debug("{}: УРОН: {}",
                this.getClass().getSimpleName().toUpperCase(), damage);

        takingDamage.setHealth(takingDamage.getHealth() - damage);
        if (takingDamage instanceof Player) {
            SaveAndLoad.INSTANCE.save((Player) takingDamage);
        }

        return damage;
    }

    private void win(Person winner, Person loser) {

        if (winner instanceof Player) {
            if (loser instanceof NPC) {
                logger.info("{}: ИГРОК ID: {}, ПОБЕДИЛ",
                        this.getClass().getSimpleName().toUpperCase(), winner.getId());
                playerWin((Player) winner, (NPC) loser);
            }
        } else if (winner instanceof NPC) {
            if (loser instanceof Player) {
                logger.info("{}: ИГРОК ID: {}, ПРОИГРАЛ",
                        this.getClass().getSimpleName().toUpperCase(), loser.getId());
                npcWin((NPC) winner, (Player) loser);
            }
        }

        FightWorker.INSTANCE.endFight(this);

    }

    private void playerWin(Player player, NPC npc) {

        player.setCoins(player.getCoins() + npc.getCoins());
        SaveAndLoad.INSTANCE.save(player);

        logger.info("{}: ИГРОК ID: {} ПОЛУЧАЕТ {} {}",
                this.getClass().getSimpleName().toUpperCase(), player.getId(),
                npc.getCoins(), Parameters.COINS.getName());

        String title = String.format("""
                %s побеждает
                """, PersonWorker.INSTANCE.getPersonName(player));
        String description = String.format("""
                %s находит у %s %s %s
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(player), PersonWorker.INSTANCE.getPersonName(npc),
                npc.getCoins(), Parameters.COINS.getName(),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(messageChannel, title)
                .setDescription(description).build().replyMessage();

    }

    private void npcWin(NPC npc, Player player) {

        player.setHealth(0);
        player.setCoins(player.getCoins() - npc.getCoins());
        SaveAndLoad.INSTANCE.save(player);

        logger.info("{}: ИГРОК ID: {} ТЕРЯЕТ {} {}",
                this.getClass().getSimpleName().toUpperCase(), player.getId(),
                npc.getCoins(), Parameters.COINS.getName());

        String title = String.format("""
                %s побеждает
                """, PersonWorker.INSTANCE.getPersonName(npc));
        String description = String.format("""
                %s забирает у %s %s %s
                
                %s
                """, PersonWorker.INSTANCE.getPersonName(npc), PersonWorker.INSTANCE.getPersonName(player),
                npc.getCoins(), Parameters.COINS.getName(),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(messageChannel, title)
                .setDescription(description).build().replyMessage();

    }

    public Person getAttacker() {
        return attacker;
    }

    public Person getDefender() {
        return defender;
    }
}
