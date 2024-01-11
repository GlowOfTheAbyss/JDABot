package org.glow.discordBot.items.legs;

public enum LegsFactory {

    INSTANCE;

    public Legs getEmptyLegs() {
        return new Legs(
                "Пусто", 0, 0
        );
    }

    public Legs getIronLegsArmor() {
        return new Legs(
                "Броня для ног из железа", 1_600, 4
        );
    }

    public Legs getWhiteIronLegsArmor() {
        return new Legs(
                "Броня для ног из белого железа", 4_000, 10
        );
    }

}
