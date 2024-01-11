package org.glow.discordBot.entities;

public enum InventoryWorker {

    INSTANCE;

    public int getArmor(Inventory inventory) {
        return inventory.getHead().getArmor() + inventory.getBody().getArmor()
                + inventory.getLegs().getArmor() + inventory.getHandLeft().getArmor();
    }

    public int getAttack(Inventory inventory) {
        return inventory.getHandRight().getAttack();
    }

}
