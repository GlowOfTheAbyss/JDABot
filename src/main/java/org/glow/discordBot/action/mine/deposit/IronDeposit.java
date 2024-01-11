package org.glow.discordBot.action.mine.deposit;

import org.glow.discordBot.entities.Player;

import java.util.Random;

public class IronDeposit extends Deposit {

    public IronDeposit(Player player) {
        super(player);
        setName("Залежь железа");
        int coins = new Random().nextInt(1, 4);
        coins += (int) (0.5 * player.getStrength().getValue());
        coins += (int) (0.5 * player.getEndurance().getValue());
        coins *= 10;
        setCoins(coins);
    }

}
