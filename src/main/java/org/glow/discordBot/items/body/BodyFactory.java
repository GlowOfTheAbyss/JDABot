package org.glow.discordBot.items.body;

public enum BodyFactory {

    INSTANCE;

    public Body getEmptyBody() {
        return new Body(
                "Пусто", 0, 0
        );
    }

    public Body getIronBodyArmor() {
        return new Body(
                "Нагрудная броня из железа", 2_400, 6
        );
    }

    public Body getWhiteIronBodyArmor(){
        return new Body(
                "Нагрудная броня из белого железа", 5_600, 14
        );
    }

}
