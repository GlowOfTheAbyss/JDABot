package org.glow.discordBot.items.legs;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.items.Armor;

public class Legs extends Armor {

    public Legs() {}

    public Legs(String name, int price, int armor) {
        super(name, price, armor);
    }

    @Override
    public void equip(Inventory inventory) {
        Legs legs = inventory.getLegs();
        inventory.setLegs(this);
        inventory.getBag().removeItem(legs);

        if (!legs.equals(LegsFactory.INSTANCE.getEmptyLegs())) {
            inventory.getBag().addItem(legs);
        }
    }

    @Override
    public void unequip(Inventory inventory) {
        inventory.setLegs(LegsFactory.INSTANCE.getEmptyLegs());
        inventory.getBag().addItem(this);
    }
}
