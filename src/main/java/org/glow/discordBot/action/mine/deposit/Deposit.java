package org.glow.discordBot.action.mine.deposit;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;
import org.glow.discordBot.fileworkers.SaveAndLoad;
import org.glow.discordBot.message.MessageSender;
import org.glow.discordBot.message.Parameters;
import org.glow.discordBot.message.TextCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Deposit {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Player player;
    private String name;
    private int coins;

    public Deposit(Player player) {
        this.player = player;
    }

    public void mine(SlashCommandInteractionEvent event) {

        logger.info("{}: СТАРТ", getName().toUpperCase());

        player.setCoins(player.getCoins() + coins);
        SaveAndLoad.INSTANCE.save(player);
        logger.info("{}: ИГРОК ID: {} ПОЛУЧАЕТ {} {}",
                getName().toUpperCase(), player.getId(), coins, Parameters.COINS.getName().toUpperCase());

        String title = String.format("""
                %s находит %s
                """, PersonWorker.INSTANCE.getPersonName(player), getName());

        String description = String.format("""
                        %s получает %s %s
                                        
                        %s
                        """, PersonWorker.INSTANCE.getPersonName(player), coins, Parameters.COINS.getName(),
                TextCreator.INSTANCE.getPlayerParameters(player));

        new MessageSender.MessageBuilder(event.getHook(), title)
                .setDescription(description)
                .build().replyMessage();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

}
