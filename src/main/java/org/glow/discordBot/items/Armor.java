package org.glow.discordBot.items;

public abstract class Armor extends Item implements Equipped, Unequipped {

    private int armor;

    public Armor() {}

    public Armor(String name, int price, int armor) {
        super(name, price);
        this.armor = armor;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

}
