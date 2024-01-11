package org.glow.discordBot.entities;

import org.glow.discordBot.Bot;
import org.glow.discordBot.entities.npc.NPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public enum PersonWorker {

    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Set<Player> players = new HashSet<>();

    public Set<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public int getPersonLevel(Person person) {
        logger.debug("{}: ИГРОКА ID: {}, ЗАПРОС УРОВНЯ",
                this.getClass().getSimpleName().toUpperCase(), person.getId());

        return person.getStrength().getValue() + person.getEndurance().getValue() + person.getAgility().getValue()
                + person.getIntelligence().getValue() + person.getPerception().getValue() + person.getLuck().getValue();
    }

    public int getPersonCombatLevel(Person person) {
        logger.debug("{}: ИГРОК ID: {}, ЗАПРОС БОЕВОГО УРОВЕНЯ",
                this.getClass().getSimpleName().toUpperCase(), person.getId());

        return person.getStrength().getValue() + person.getEndurance().getValue() + person.getAgility().getValue();
    }

    public String getPersonName(Person person) {
        if (person instanceof Player) {
            logger.debug("{}: ИГРОК ID: {}, ЗАПРОС ИМЕНИ",
                    this.getClass().getSimpleName().toUpperCase(), person.getId());

            try {
                // пытаемся из кеша достать имя игрока по ID
                String name = Objects.requireNonNull(Bot
                                .getSystemsWorker()
                                .getJda()
                                .getUserById(person.getId())).getEffectiveName();

                logger.debug("{}: ИГРОК ID: {}, ИМЯ: {}",
                        this.getClass().getSimpleName().toUpperCase(), person.getId(), name);

                return name;
            } catch (NullPointerException exception) {
                logger.warn("{}: ИГРОК ID: {}, ИМЯ НЕ НАЙДЕНО",
                        this.getClass().getSimpleName().toUpperCase(), person.getId());

                throw new IllegalArgumentException("Имя игрока не найдено");
            }

        } else if (person instanceof NPC) {
            logger.debug("{}: NPC NAME: {}, НАЙДЕНО",
                    this.getClass().getSimpleName().toUpperCase(), ((NPC) person).getName());

            return ((NPC) person).getName();
        } else {

            logger.warn("{}: ПРЕДОСТАВЛЕННЫЙ PERSON НЕ ЯВЛЯЕТСЯ ИЗВЕСТНЫМ ПОДКЛАССОМ",
                    this.getClass().getSimpleName().toUpperCase());

            throw new IllegalArgumentException("Сущность не определена");
        }
    }

    public Player getPlayerById(String id) {
        logger.debug("{}: ИГРОК ID: {}, ПОИСК ПО ID",
                this.getClass().getSimpleName().toUpperCase(), id);

        for (Player player : players) {
            if (player.getId().equals(id)) {
                logger.debug("{}: ИГРОК ID: {}, НАЙДЕН",
                        this.getClass().getSimpleName().toUpperCase(), player.getId());

                return player;
            }
        }

        logger.warn("{}: ИГРОК ID: {}, НЕ НАЙДЕН", this.getClass().getSimpleName().toUpperCase(), id);

        throw new IllegalArgumentException("Игрок не найден");
    }

    public int getPlayerMaxHealth(Player player) {
        logger.debug("{}: ИГРОК ID: {}, МАКСИМАЛЬНОЕ ЗДОРОВЬЕ: {}", this.getClass().getSimpleName().toUpperCase(),
                player.getId(), 10 + player.getEndurance().getValue() * 10);

        return (10 + player.getEndurance().getValue()) * 10;
    }

    public int getPlayerMaxMana(Player player) {
        logger.debug("{}: ИГРОК ID: {}, МАКСИМАЛЬНАЯ МАНА: {}", this.getClass().getSimpleName().toUpperCase(),
                player.getId(), (10 + ((int) (0.5 * player.getIntelligence().getValue()))) * 10);

        return (10 + ((int) (0.5 * player.getIntelligence().getValue()))) * 10;
    }

}
