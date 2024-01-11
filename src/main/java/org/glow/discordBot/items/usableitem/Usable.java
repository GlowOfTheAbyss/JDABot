package org.glow.discordBot.items.usableitem;

import org.glow.discordBot.entities.Player;

public interface Usable {

    String getInfo();

    void use(Player player);

}
