package org.glow.discordBot.items.handright;

public enum HandRightFactory {

    INSTANCE;

    public HandRight getEmptyRightHand() {
        return new HandRight(
                "Пусто", 0, 0
        );
    }

    public HandRight getDullBlade() {
        return new HandRight(
                "Тупой меч", 4_500, 10
        );
    }

    public HandRight getSilverSword(){
        return new HandRight(
                "Серебряный меч", 9_000, 20
        );
    }

}
