package org.glow.discordBot.action.mine.deposit;

import org.glow.discordBot.entities.Player;

import java.util.Random;

public class WhiteIronDeposit extends Deposit {

    public WhiteIronDeposit(Player player) {
        super(player);
        setName("Залежь Белого железа");
        int coins = new Random().nextInt(2, 6);
        coins += (int) (0.5 * player.getStrength().getValue());
        coins += player.getEndurance().getValue();
        coins *= 10;
        setCoins(coins);
    }

}
