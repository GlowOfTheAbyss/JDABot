package org.glow.discordBot.message.characteristic;

public class Characteristic {

    private String name;
    private int value;

    public Characteristic() {
    }

    public Characteristic(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
