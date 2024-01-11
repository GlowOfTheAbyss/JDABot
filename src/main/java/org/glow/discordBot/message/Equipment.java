package org.glow.discordBot.message;

public enum Equipment {

    HEAD("Голова"),
    BODY("Тело"),
    LEGS("Ноги"),
    HAND_RIGHT("Правая рука"),
    HAND_LEFT("Левая рука"),

    ATTACK("Атака"),
    DEFEND("Защита");

    private final String name;

    Equipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
