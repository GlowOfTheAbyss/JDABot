package org.glow.discordBot.fileworkers;

import java.io.IOException;
import java.nio.file.Files;

public class TokenFinder {

    // имя файла с токеном
    private final String tokenFileName = "secret";

    public String findToken() {
        try {
            return Files.readAllLines(new PathGenerator(tokenFileName).getTokenPath()).get(0);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getTokenFileName() {
        return tokenFileName;
    }

}
