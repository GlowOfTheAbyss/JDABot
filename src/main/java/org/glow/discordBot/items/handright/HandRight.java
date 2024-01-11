package org.glow.discordBot.items.handright;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.items.Weapon;

public class HandRight extends Weapon {

    public HandRight() {}

    public HandRight(String name, int price, int attack) {
        super(name, price, attack);
    }

    @Override
    public void equip(Inventory inventory) {
        HandRight handRight = inventory.getHandRight();
        inventory.setHandRight(this);
        inventory.getBag().removeItem(this);

        if (!handRight.equals(HandRightFactory.INSTANCE.getEmptyRightHand())) {
            inventory.getBag().addItem(handRight);
        }
    }

    @Override
    public void unequip(Inventory inventory) {
        inventory.setHandRight(HandRightFactory.INSTANCE.getEmptyRightHand());
        inventory.getBag().addItem(this);
    }

}
