package org.glow.discordBot;

public class Bot {

    private static SystemsWorker systemsWorker;

    public static void main(String[] args) {

        systemsWorker = new SystemsWorker();

    }

    public static SystemsWorker getSystemsWorker() {
        return systemsWorker;
    }

}
