package com.dylanpdx.retro64;

import com.dylanpdx.retro64.sm64.libsm64.Libsm64Library;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;

// import java.io.DataInputStream;
// import java.io.File;
import java.io.IOException;
// import java.io.InputStream;



public class TexGenerator {

    public static NativeImage convertRawTexToMChar(NativeImage img){
        NativeImage mCharBg = new NativeImage(Libsm64Library.SM64_TEXTURE_WIDTH, Libsm64Library.SM64_TEXTURE_HEIGHT, false);
        mCharBg.fillRect(0, 0, Libsm64Library.SM64_TEXTURE_WIDTH, Libsm64Library.SM64_TEXTURE_HEIGHT, 0x00000000); // fill entire image with transparent black
        mCharBg.fillRect(0,0,64,32,0xFF000000);
        mCharBg.fillRect(64,0,32,32,0xFFFF0000);
        mCharBg.fillRect(96,0,94,32,0xFF0000FF);
        mCharBg.fillRect(190,0,386,64,0xFF79C1FE);

        overlayImage(mCharBg, img);

        return mCharBg;
    }

    public static NativeImage appendSteveStuffToTex(NativeImage input){
        // assumes 320x64 input
        try {
            var steveAtlasIn = Minecraft.getInstance().getResourceManager().getResource(textureManager.steveAtlas).get().open();
            var steveAtlas = NativeImage.read(steveAtlasIn);
            overlayImage(input,steveAtlas,192,0,192,0,128,64);
            return input;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate Luigi texture from ROM data
     * @param img ROM data
     * @return Luigi texture
     */
    public static NativeImage convertMCharTexToLuigi(NativeImage img){
        // duplicate the img first
        NativeImage luigiBg = new NativeImage(Libsm64Library.SM64_TEXTURE_WIDTH, Libsm64Library.SM64_TEXTURE_HEIGHT, false);
        luigiBg.fillRect(0, 0, Libsm64Library.SM64_TEXTURE_WIDTH, Libsm64Library.SM64_TEXTURE_HEIGHT, 0x00000000); // fill entire image with transparent black
        overlayImage(luigiBg, img);
        try {
            var LAtlasInput = Minecraft.getInstance().getResourceManager().getResource(textureManager.luigiAtlas).get().open();
            var LAtlas = NativeImage.read(LAtlasInput);
            overlayImage(luigiBg, LAtlas);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return luigiBg;
    }

    /**
     * take NativeImage A and overlay NativeImage B on top, taking into account Alpha, assuming the image is in ABGR
     * @param imgA NativeImage A
     * @param imgB NativeImage B
     */
    public static void overlayImage(NativeImage imgA, NativeImage imgB){
        for(int x = 0; x < imgA.getWidth(); x++){
            for(int y = 0; y < imgA.getHeight(); y++){
                imgA.blendPixel(x, y, imgB.getPixelRGBA(x, y));

            }
        }

    }

    /**
     * take NativeImage A, and overlay NativeImage B on top, taking into account Alpha, assuming the image is in ABGR
     * @param imgA NativeImage A
     * @param imgB NativeImage B
     * @param aX X coordinate of A
     * @param aY Y coordinate of A
     * @param bX X coordinate of B
     * @param bY Y coordinate of B
     * @param width width of the overlay
     * @param height height of the overlay
     */
    public static void overlayImage(NativeImage imgA, NativeImage imgB, int aX, int aY, int bX, int bY, int width, int height){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                imgA.blendPixel(aX + x, aY + y, imgB.getPixelRGBA(bX + x, bY + y));

            }
        }
    }
}

