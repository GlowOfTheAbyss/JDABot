package org.glow.discordBot.items.head;

public enum HeadFactory {

    INSTANCE;

    public Head getEmptyHead() {
        return new Head(
                "Пусто", 0, 0
        );
    }

    public Head getIronHeadArmor() {
        return new Head(
                "Шлем из железа", 800, 2
        );
    }

    public Head getWhiteIronHeadArmor(){
        return new Head(
                "Шлем из белого железа", 2_400, 6
        );
    }

}
