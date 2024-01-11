package org.glow.discordBot.fileworkers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glow.discordBot.entities.PersonWorker;
import org.glow.discordBot.entities.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public enum SaveAndLoad {

    INSTANCE;

    private final Path saveFolderPath = new PathGenerator(new TokenFinder().getTokenFileName()).getSaveFolderPath();

    public void save(Player player) {

        ObjectMapper objectMapper = new ObjectMapper();

        Path savePath = saveFolderPath.resolve(player.getId() + ".json");

        try {
            objectMapper.writeValue(new File(savePath.toUri()), player);
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception);
        }

    }

    public void load() {

        ObjectMapper objectMapper = new ObjectMapper();

        try (DirectoryStream<Path> files = Files.newDirectoryStream(saveFolderPath)) {

            for (Path filePath : files) {
                if (Files.notExists(filePath)) {
                    return;
                }

                Player player = objectMapper.readValue(new File(filePath.toUri()), Player.class);
                PersonWorker.INSTANCE.addPlayer(player);

            }

        } catch (IOException exception) {
            throw new IllegalArgumentException(exception);
        }

    }

}
