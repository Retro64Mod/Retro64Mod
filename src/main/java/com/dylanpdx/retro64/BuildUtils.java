package com.dylanpdx.retro64;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BuildUtils {

    private static String sourceRepoURL = "https://github.com/Retro64Mod/libsm64-retro64.git";
    private static String sourceRepo="libsm64-retro64";
    private static String baseArgs;
    private static String repoDir;
    private static String portPath;
    public static boolean buildComplete=false;


    public static void Compile(String MSYS2_dir) throws IOException {
        repoDir = Paths.get(MSYS2_dir,"home",System.getProperty("user.name"),"libsm64-retro64").toString();
        portPath = Paths.get(repoDir,"dist").toString();
        if (Files.exists(Paths.get(repoDir))) {
            // delete directory
            FileUtils.deleteDirectory(Paths.get(repoDir).toFile());
        }
        Retro64.LOGGER.info("Initial setup complete");

        // setup MSYS2
        baseArgs="";
        //baseArgs+="-w hide ";
        //baseArgs+="-h always ";
        baseArgs+="/bin/env MSYSTEM=MINGW64 /bin/bash -l -c '";

        // check dependences
        executeMSYS2("pacman -S --needed --noconfirm git make python3 mingw-w64-x86_64-gcc",MSYS2_dir);

        Retro64.LOGGER.info("Cloning source repo");
        // clone repo
        executeMSYS2("git clone " + sourceRepoURL + " " + sourceRepo,MSYS2_dir);

        String compileCommand = "cd ./" + sourceRepo + " && make";
        executeMSYS2(compileCommand,MSYS2_dir);

        // verify portPath/sm64.dll exists
        if (!Files.exists(Paths.get(portPath,"sm64.dll"))) {
            Retro64.LOGGER.info("sm64.dll not found");
        }else{
            // copy it to mods/sm64.dll
            FileUtils.copyFile(Paths.get(portPath,"sm64.dll").toFile(),Paths.get("mods","sm64.dll").toFile());
            buildComplete=true;
            Minecraft.getInstance().setScreen(new TitleScreen());
        }

    }

    private static void executeMSYS2(String command,String MSYS2_dir) throws IOException {
        // MSYS2_DIR\\usr\\bin\\mintty.exe
        var cmd = Paths.get(MSYS2_dir,"usr","bin","mintty.exe").toString()+ " "+baseArgs + " "+command+"'";
        String[] commandArray = cmd.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        while (process.isAlive()) {
            System.out.println(process.getInputStream().read());
        }
        Retro64.LOGGER.info("MSYS2 command complete");
    }

}
