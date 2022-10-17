package org.skhengui.utils;

import org.skhengui.dao.SyncOnExit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class SynchronizeJsonData {

    public static void configureDataFiles() {
        Path dataDir = Paths.get(DataConstants.JSON_DATA_FOLDER);

        if (Files.exists(dataDir)) {
            return;
        }

        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sync(SyncOnExit... items) {
        Arrays.stream(items).forEach(SyncOnExit::sync);
    }
}
