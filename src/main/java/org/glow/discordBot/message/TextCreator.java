package org.glow.discordBot.message;

import org.glow.discordBot.entities.InventoryWorker;
import org.glow.discordBot.entities.Person;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.items.Item;
import org.glow.discordBot.items.skill.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TextCreator {

    INSTANCE;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getPlayerParameters(Player player) {
        logger.debug("{}: ИГРОК ID: {}, ЗАПРОС ПАРАМЕТРОВ",
                this.getClass().getSimpleName().toUpperCase(), player.getId());

        return String.format("""
                %s: %s
                %s: %s
                %s: %s
                %s: %s
                """,
                Parameters.COINS.getName(), player.getCoins(),
                Parameters.ENERGY.getName(), player.getEnergy(),
                Parameters.HEALTH.getName(), player.getHealth(),
                Parameters.MANA.getName(), player.getMana());

    }

    public String getPlayerCharacteristic(Player player) {
        logger.info("{}: ИГРОК ID: {}, ЗАПРОС ХАРАКТЕРИСТИК",
                this.getClass().getSimpleName().toUpperCase(), player.getId());

        return String.format("""
                %s: %s
                %s: %s
                %s: %s
                %s: %s
                %s: %s
                %s: %s
                """,
                player.getStrength().getName(), player.getStrength().getValue(),
                player.getEndurance().getName(), player.getEndurance().getValue(),
                player.getAgility().getName(), player.getAgility().getValue(),
                player.getIntelligence().getName(), player.getIntelligence().getValue(),
                player.getPerception().getName(), player.getPerception().getValue(),
                player.getLuck().getName(), player.getLuck().getValue());
    }

    public String getPlayerEquippedItems(Player player) {
        logger.info("{}: ИГРОК ID: {}, ЗАПРОС ЭКИПЕРОВАННЫХ ПРЕДМЕТОВ",
                this.getClass().getSimpleName().toUpperCase(), player.getId());

        return String.format("""
                %s: %s
                %s: %s
                
                %s: %s
                %s: %s
                %s: %s
                %s: %s
                %s: %s
                """,
                Equipment.DEFEND.getName(), InventoryWorker.INSTANCE.getArmor(player.getInventory()),
                Equipment.ATTACK.getName(), InventoryWorker.INSTANCE.getAttack(player.getInventory()),

                Equipment.HEAD.getName(), player.getInventory().getHead().getName(),
                Equipment.BODY.getName(), player.getInventory().getBody().getName(),
                Equipment.LEGS.getName(), player.getInventory().getLegs().getName(),
                Equipment.HAND_RIGHT.getName(), player.getInventory().getHandRight().getName(),
                Equipment.HAND_LEFT.getName(), player.getInventory().getHandLeft().getName());

    }

    public String getPlayerInventory(Player player) {
        logger.info("{}: ИГРОК ID: {}, ЗАПРОС СУМКИ",
                this.getClass().getSimpleName().toUpperCase(), player.getId());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Сумка: ");

        if (player.getInventory().getBag().getItems().isEmpty()) {
            stringBuilder.append("Пусто");
        } else {
            for (Item items : player.getInventory().getBag().getItems()) {
                stringBuilder.append(" | ")
                        .append(items.getName());
            }
            stringBuilder.append(" | ");
        }

        return stringBuilder.toString();

    }

    public String getPlayerSkillBook(Player player) {
        logger.info("{}: ИГРОК ID: {}, ЗАПРОС СКИЛЛОВ",
                this.getClass().getSimpleName().toUpperCase(), player.getId());

        StringBuilder stringBuilder = new StringBuilder();

        if (player.getSkillBook().getSkills().isEmpty()) {
            stringBuilder.append("Пусто");
        } else {
            for (Skill skill : player.getSkillBook().getSkills()) {

                stringBuilder.append(String.format("""
                        %s | %s %s
                        %s
                        """, skill.getName(), skill.getCoastInMana(), Parameters.MANA.getName(),
                        skill.getSpellInfo()));

            }
        }

        return stringBuilder.toString();

    }

    public String getBattleParameters(Person attacker, Person defender) {
        logger.info("{}: ID: {}, ID: {}, ЗАПРОС БОЕВЫХ ПАРАМЕТРОВ",
                this.getClass().getSimpleName().toUpperCase(), attacker.getId(), defender.getId());

        return String.format("""
                %s %s: %s
                %s %s: %s
                """,
                PersonWorker.INSTANCE.getPersonName(attacker), Parameters.HEALTH.getName(), attacker.getHealth(),
                PersonWorker.INSTANCE.getPersonName(defender), Parameters.HEALTH.getName(), defender.getHealth());

    }

}
