package org.glow.discordBot.action.mine;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.action.Action;
import org.glow.discordBot.action.chest.CommonChest;
import org.glow.discordBot.action.chest.ExquisiteChest;
import org.glow.discordBot.action.mine.deposit.IronDeposit;
import org.glow.discordBot.action.mine.deposit.WhiteIronDeposit;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class MtTianhengMine implements Action {

    private SlashCommandInteractionEvent event;
    private Player player;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "Гора Тяньхен";
    }

    @Override
    public String getDescription() {
        return "Заброшенная шахта вблизи города, монстров нету, вероятность найти что то необычное довольно мала";
    }

    @Override
    public void start(SlashCommandInteractionEvent event, Player player) {

        logger.info("АКТИВНОСТЬ {}: СТАРТ", getName().toUpperCase());

        this.event = event;
        this.player = player;

        player.setEnergy(player.getEnergy() - 1);
        SaveAndLoad.INSTANCE.save(player);
        logger.info("АКТИВНОСТЬ {}: ИГРОК ID: {} ТЕРЯЕТ 1 ЭНЕРГИЮ", getName().toUpperCase(), player.getId());

        int random = new Random().nextInt(1, 101);
        logger.debug("АКТИВНОСТЬ {}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int chestChance = 3 + player.getLuck().getValue();
        chestChance -= (int) (0.1 * PersonWorker.INSTANCE.getPersonLevel(player));
        logger.debug("АКТИВНОСТЬ {}: ШАНС СУНДУКА {}", getName().toUpperCase(), chestChance);

        if (chestChance >= random) {
            chest();
        } else {
            deposit();
        }

    }

    private void deposit() {

        logger.info("АКТИВНОСТЬ {}: ЗАЛЕЖЬ", getName().toUpperCase());

        int random = new Random().nextInt(1, 101);
        logger.debug("АКТИВНОСТЬ {}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int whiteIronDepositChance = 10;
        whiteIronDepositChance += player.getPerception().getValue();
        whiteIronDepositChance -= (int) (0.1 * PersonWorker.INSTANCE.getPersonLevel(player));
        logger.debug("АКТИВНОСТЬ {}: ШАНС ЗАЛЕЖИ БЕЛОГО ЖЕЛЕЗА {}", getName().toUpperCase(), whiteIronDepositChance);

        if (whiteIronDepositChance >= random) {
            new WhiteIronDeposit(player).mine(event);
        } else {
            new IronDeposit(player).mine(event);
        }

    }

    private void chest() {

        logger.info("АКТИВНОСТЬ {}: СУНДУК", getName().toUpperCase());

        int random = new Random().nextInt(1, 101);
        logger.debug("АКТИВНОСТЬ {}: СЛУЧАЙНОЕ ЧИСЛО {}", getName().toUpperCase(), random);
        int exquisiteChestChance = 5;
        exquisiteChestChance += (int) (0.5 * player.getLuck().getValue());
        exquisiteChestChance -= (int) (0.05 * PersonWorker.INSTANCE.getPersonLevel(player));
        logger.debug("АКТИВНОСТЬ {}: ШАНС БОГАТОГО СУНДУКА {}", getName().toUpperCase(), exquisiteChestChance);

        if (exquisiteChestChance >= random) {
            new ExquisiteChest(player).open(event);
        } else {
            new CommonChest(player).open(event);
        }

    }

}
