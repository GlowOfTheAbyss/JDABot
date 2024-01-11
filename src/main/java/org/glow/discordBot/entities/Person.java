package org.glow.discordBot.entities;

import org.glow.discordBot.message.characteristic.Characteristic;

public class Person {

    private String id;

    private int health;
    private int mana;
    private int energy;
    private int coins;

    private Characteristic strength;
    private Characteristic endurance;
    private Characteristic agility;
    private Characteristic intelligence;
    private Characteristic perception;
    private Characteristic luck;

    private Inventory inventory;
    private SkillBook skillBook;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Characteristic getStrength() {
        return strength;
    }

    public void setStrength(Characteristic strength) {
        this.strength = strength;
    }

    public Characteristic getEndurance() {
        return endurance;
    }

    public void setEndurance(Characteristic endurance) {
        this.endurance = endurance;
    }

    public Characteristic getAgility() {
        return agility;
    }

    public void setAgility(Characteristic agility) {
        this.agility = agility;
    }

    public Characteristic getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Characteristic intelligence) {
        this.intelligence = intelligence;
    }

    public Characteristic getPerception() {
        return perception;
    }

    public void setPerception(Characteristic perception) {
        this.perception = perception;
    }

    public Characteristic getLuck() {
        return luck;
    }

    public void setLuck(Characteristic luck) {
        this.luck = luck;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public SkillBook getSkillBook() {
        return skillBook;
    }

    public void setSkillBook(SkillBook skillBook) {
        this.skillBook = skillBook;
    }
}
