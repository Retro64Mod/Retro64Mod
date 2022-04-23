package com.dylanpdx.retro64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Extract audio assets
 */
public class assetsExtract {

    public static String extractToTmp(File rom) throws IOException, InterruptedException {
        Retro64.LOGGER.info("Extracting game assets to temporary directory");
        var tempDir = Files.createTempDirectory("retro64");
        File exeFile = binExtract.getGenfilesPath();//Path.of("mods","genfiles.exe").toFile();
        // check if exe exists
        if (exeFile == null || !exeFile.exists()) {
            Retro64.LOGGER.info("exe file not found");
            return null;
        }
        // run exe with rom as 1st argument, and temp dir as working dir
        Retro64.LOGGER.info("Running extraction with rom as 1st argument, and temp dir as working dir");
        ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath(), rom.getAbsolutePath());
        pb.directory(tempDir.toFile());
        Process p = pb.start();
        p.waitFor();
        Retro64.LOGGER.info("Extraction complete");
        // return all files in temp dir
        if (tempDir.toFile().listFiles().length!=4)
            return null;
        return tempDir.toString();
    }

}
