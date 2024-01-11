package org.glow.discordBot.action.fight;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum FightWorker {

    INSTANCE;

    private final List<Fight> fights = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void createFight(Person attacker, Person defender, SlashCommandInteractionEvent event) {

        logger.info("{}: СОЗДАЕТСЯ НОВАЯ БИТВА {} ПРОТИВ {}", this.getClass().getSimpleName().toUpperCase(),
                attacker.getId(), defender.getId());

        Fight fight = new Fight(attacker, defender, event);
        fights.add(fight);
        fight.start();

    }

    public void endFight(Fight fight) {
        fights.remove(fight);
    }

    public boolean personInFight(Person player) {
        for (Fight fight : fights) {
            if (fight.getAttacker().equals(player)) {
                return true;
            }
            if (fight.getDefender().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public Fight getFightByPerson(Person person) {
        for (Fight fight : fights) {
            if (fight.getAttacker().equals(person)) {
                return fight;
            }
            if (fight.getDefender().equals(person)) {
                return fight;
            }
        }
        throw new IllegalArgumentException("Сражение не найдено");
    }


}
