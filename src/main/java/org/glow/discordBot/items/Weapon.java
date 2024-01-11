package org.glow.discordBot.items;

public abstract class Weapon extends Item implements Equipped, Unequipped {

    int attack;

    public Weapon() {
    }

    public Weapon(String name, int price, int attack) {
        super(name, price);
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

}
