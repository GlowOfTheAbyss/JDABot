package org.glow.discordBot.items.body;

import org.glow.discordBot.entities.Inventory;
import org.glow.discordBot.items.Armor;

public class Body extends Armor {

    public Body() {}

    public Body(String name, int price, int armor) {
        super(name, price, armor);
    }

    @Override
    public void equip(Inventory inventory) {
        Body body = inventory.getBody();
        inventory.setBody(this);
        inventory.getBag().removeItem(this);

        if (!body.equals(BodyFactory.INSTANCE.getEmptyBody())) {
            inventory.getBag().addItem(body);
        }
    }

    @Override
    public void unequip(Inventory inventory) {
        inventory.setBody(BodyFactory.INSTANCE.getEmptyBody());
        inventory.getBag().addItem(this);
    }

}
