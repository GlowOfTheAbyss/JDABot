package org.glow.discordBot.timeactions;

import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeAction implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static TimeAction timeAction;

    private TimeAction() {}

    @Override
    public void run() {

        PersonWorker.INSTANCE.getPlayers().forEach(player -> {

            // даем каждому игроку энергию и востонавливаем HP и MP

            if (player.getEnergy() < 5) {
                player.setEnergy(player.getEnergy() + 1);
            }

            int maxHP = PersonWorker.INSTANCE.getPlayerMaxHealth(player);
            int maxMP = PersonWorker.INSTANCE.getPlayerMaxMana(player);

            if (player.getHealth() < maxHP) {
                player.setHealth(player.getHealth() + 10);
                if (player.getHealth() > maxHP) {
                    player.setHealth(maxHP);
                }
            }

            if (player.getMana() < maxMP) {
                player.setMana(player.getMana() + 10);
                if (player.getMana() > maxMP) {
                    player.setMana(maxMP);
                }
            }

            SaveAndLoad.INSTANCE.save(player);

        });

    }

    public static TimeAction getInstance() {
        if (timeAction == null) {
            timeAction = new TimeAction();
        }
        return timeAction;
    }
}
