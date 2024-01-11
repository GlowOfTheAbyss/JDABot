package org.glow.discordBot.items.handleft;

public enum HandLeftFactory {

    INSTANCE;

    public HandLeft getEmptyHandLeft() {
        return new HandLeft(
                "Пусто", 0, 0
        );
    }

    public HandLeft getIronShield() {
        return new HandLeft(
                "Щит из железа", 1_600, 4
        );
    }

    public HandLeft getWhiteIronShield(){
        return new HandLeft(
                "Щит из белого железа", 3_200, 8
        );
    }

}
