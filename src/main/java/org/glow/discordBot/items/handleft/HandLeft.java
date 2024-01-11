package org.glow.discordBot.items.handleft;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.items.Shield;

public class HandLeft extends Shield {

    public HandLeft() {}

    public HandLeft(String name, int price, int armor) {
        super(name, price, armor);
    }

    @Override
    public void equip(Inventory inventory) {
        HandLeft handLeft = inventory.getHandLeft();
        inventory.setHandLeft(this);
        inventory.getBag().removeItem(this);

        if (!handLeft.equals(HandLeftFactory.INSTANCE.getEmptyHandLeft())) {
            inventory.getBag().addItem(handLeft);
        }
    }

    @Override
    public void unequip(Inventory inventory) {
        inventory.setHandLeft(HandLeftFactory.INSTANCE.getEmptyHandLeft());
        inventory.getBag().addItem(this);
    }
}
