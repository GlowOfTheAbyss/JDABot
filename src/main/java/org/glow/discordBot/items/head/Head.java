package org.glow.discordBot.items.head;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.items.Armor;

public class Head extends Armor {

    public Head() {}

    public Head(String name, int price, int armor) {
        super(name, price, armor);
    }

    @Override
    public void equip(Inventory inventory) {
        Head head = inventory.getHead();
        inventory.setHead(this);
        inventory.getBag().removeItem(this);

        if (!head.equals(HeadFactory.INSTANCE.getEmptyHead())) {
            inventory.getBag().addItem(head);
        }
    }

    @Override
    public void unequip(Inventory inventory) {
        inventory.setHead(HeadFactory.INSTANCE.getEmptyHead());
        inventory.getBag().addItem(this);
    }
}
