package org.glow.discordBot.fileworkers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathGenerator {

    private final Path tokenPath;
    private final Path saveFolderPath;

    public PathGenerator(String tokenFileName) {

        Path resourcePath = Path.of("src/main/resources");

        tokenPath = Path.of(resourcePath + "/" + tokenFileName + ".txt").toAbsolutePath();
        Path saveFolderPatch = Path.of(resourcePath + "/saves");

        if (Files.notExists(saveFolderPatch)) {
            try {
                Files.createDirectory(saveFolderPatch);
                this.saveFolderPath = saveFolderPatch;
            } catch (IOException exception) {
                throw new IllegalArgumentException(exception);
            }
        } else {
            this.saveFolderPath = saveFolderPatch;
        }

    }

    public Path getTokenPath() {
        return tokenPath;
    }

    public Path getSaveFolderPath() {
        return saveFolderPath;
    }
}
