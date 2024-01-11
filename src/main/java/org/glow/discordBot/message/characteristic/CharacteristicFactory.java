package org.glow.discordBot.message.characteristic;

public enum CharacteristicFactory {

    INSTANCE;

    public Characteristic createStrength() {
        return new Characteristic("Сила", 0);
    }

    public Characteristic createEndurance() {
        return new Characteristic("Выносливость", 0);
    }

    public Characteristic createAgility() {
        return new Characteristic("Ловкость", 0);
    }

    public Characteristic createIntelligence() {
        return new Characteristic("Интеллект", 0);
    }

    public Characteristic createPerception() {
        return new Characteristic("Внимание", 0);
    }

    public Characteristic createLuck() {
        return new Characteristic("Удача", 0);
    }

}
