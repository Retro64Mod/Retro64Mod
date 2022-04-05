package com.dylanpdx.retro64;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Extract audio assets
 */
public class assetsExtract {

    public static String extractToTmp(File rom) throws IOException, InterruptedException {
        var tempDir = Files.createTempDirectory("retro64");
        if (SystemUtils.IS_OS_WINDOWS) return windows(tempDir, rom);
        else if (SystemUtils.IS_OS_LINUX) return linux(tempDir, rom);
        // what do we do with mac?
        else return null;
    }

    private static String windows(Path tempDir, File rom) throws IOException, InterruptedException {
        File exeFile = Path.of("mods","genfiles.exe").toFile();
        if (!exeFile.exists()) {
            Retro64.LOGGER.info("exe file not found");
            return null;
        }
        ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath(), rom.getAbsolutePath());
        pb.directory(tempDir.toFile());
        Process p = pb.start();
        p.waitFor();
        // return all files in temp dir
        if (tempDir.toFile().listFiles().length!=4)
            return null;
        return tempDir.toString();
    }

    private static String linux(Path tempDir, File rom) throws IOException, InterruptedException {
        // theres probably a better method to check for wine
        // wine is tested and works flawlessly btw
        boolean useWine = false;
        File exeFile;
        if (new File("/usr/bin/wine").exists() || new File("/usr/local/bin/wine").exists()) {
            exeFile = Path.of("mods", "genfiles.exe").toFile();
            useWine = true;
        } else exeFile = Path.of("mods", "genfiles.sh").toFile();
        if (!exeFile.exists()) {
            Retro64.LOGGER.info("exe file not found");
            return null;
        }
        List<String> commands = Lists.newArrayList(exeFile.getAbsolutePath(), rom.getAbsolutePath());
        if (useWine) commands.add(0, "wine");
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.inheritIO().directory(tempDir.toFile());
        Process p = pb.start();
        p.waitFor();
        // return all files in temp dir
        if (tempDir.toFile().listFiles().length!=4)
            return null;
        return tempDir.toString();
    }
}
