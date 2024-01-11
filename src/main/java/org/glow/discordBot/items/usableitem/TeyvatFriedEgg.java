package org.glow.discordBot.items.usableitem;

import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeyvatFriedEgg extends Food {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TeyvatFriedEgg() {
        setName("Яичница по-тейватски");
        setPrice(50);
    }

    @Override
    public void use(Player player) {

        logger.info("{}: ИГРОК ID: {} ИСПОЛЬЗУЕТ {}",
                getClass().getSimpleName().toUpperCase(), player.getId(), getName().toUpperCase());

        player.setHealth(player.getHealth() + 50);
        if (player.getHealth() > PersonWorker.INSTANCE.getPlayerMaxHealth(player)) {
            player.setHealth(PersonWorker.INSTANCE.getPlayerMaxHealth(player));
        }
        SaveAndLoad.INSTANCE.save(player);

    }

    @Override
    public String getInfo() {
        return "Востанавливает 50 здоровья при использовании";
    }

}
