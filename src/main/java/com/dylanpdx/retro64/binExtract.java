package com.dylanpdx.retro64;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class binExtract {
    private static File genfiles_bin;
    private static File libsm64_bin;
    static boolean extracted=false;

    public static void extractBins() throws IOException {
        extracted=true;
        Retro64.LOGGER.info("Extracting libsm64 and genfiles");
        String os = "linux";
        String genFilesExe = "genfiles";
        String libsm64 = "libsm64.so";
        if (SystemUtils.IS_OS_WINDOWS){
            os = "windows";
            genFilesExe = "genfiles.exe";
            libsm64 = "sm64.dll";
        }else if (SystemUtils.IS_OS_MAC){
            os = "macos";
            libsm64 = "libsm64.dylib";
        }
        ResourceLocation libsm64Loc = new ResourceLocation(Retro64.MOD_ID,"binary/x86_64/"+os+"/"+libsm64);
        ResourceLocation genFilesExeLoc = new ResourceLocation(Retro64.MOD_ID,"binary/x86_64/"+os+"/"+genFilesExe);
        Retro64.LOGGER.info("libsm64 location identified as "+libsm64Loc.toString());
        Retro64.LOGGER.info("genfiles location identified as "+genFilesExeLoc.toString());
        // extract into temp dir
        var tempDir = Files.createTempDirectory("retro64_bin");
        var stream = Minecraft.getInstance().getResourceManager().getResource(libsm64Loc).getInputStream();
        var libPath=Path.of(tempDir.toString(),libsm64);
        Files.copy(stream, libPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        stream.close();
        Retro64.LOGGER.info("libsm64 extracted to "+libPath.toString());
        stream = Minecraft.getInstance().getResourceManager().getResource(genFilesExeLoc).getInputStream();
        var genfilesPath=Path.of(tempDir.toString(),genFilesExe);
        Files.copy(stream, genfilesPath,java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        stream.close();
        Retro64.LOGGER.info("genfiles extracted to "+genfilesPath.toString());
        genfiles_bin=genfilesPath.toFile();
        libsm64_bin=libPath.toFile();
        if (!SystemUtils.IS_OS_WINDOWS){
            // make executable
            var success = genfiles_bin.setExecutable(true);
            if (!success){
                throw new IOException("Failed to make genfiles executable");
            }
        }
    }

    public static File getLibPath(){
        if (!extracted){
            try{
                extractBins();
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
        return libsm64_bin;
    }

    public static File getGenfilesPath(){
        if (!extracted){
            try{
                extractBins();
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }
        return genfiles_bin;
    }


}
