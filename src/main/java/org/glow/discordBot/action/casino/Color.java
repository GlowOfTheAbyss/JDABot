package org.glow.discordBot.action.casino;

import java.util.List;

public enum Color {

    RED("Красное"),
    BLACK("Черное"),
    ZERO("Зеленое");

    private final String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Color getColorByInt(int integer) {

        List<Integer> reds = List.of(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);

        if (integer == 0) {
            return ZERO;
        } else if (reds.contains(integer)) {
            return RED;
        } else {
            return BLACK;
        }

    }

}
