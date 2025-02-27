package com.dylanpdx.retro64;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class textureManager {

    static HashMap<String, DynamicTexture> textureMap = new HashMap<String, DynamicTexture>();
    static DynamicTexture defaultSkin=null;

    static NativeImage native_mChar;
    static DynamicTexture mCharTexture;
    static DynamicTexture luigiTexture;
    static ResourceLocation luigiAtlas = ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID,"textures/model/luigi_atlas.png");
    static ResourceLocation steveAtlas = ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID,"textures/model/steve.png");
    static ResourceLocation necoarcAtlas = ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID,"textures/model/necoarc_atlas.png");
    static ResourceLocation vibriAtlas = ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID,"textures/model/vibri_atlas.png");
    static ResourceLocation sonicAtlas = ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID,"textures/model/sonic_atlas.png");


    public static AbstractTexture getTextureForModel(int modelID, Player player){
        return switch (modelID) {
            case 0 -> getMCharTexture();
            case 1 -> getLuigiTexture();
            case 2 -> getPlayerTexture(player);//Minecraft.getInstance().getTextureManager().getTexture(steveTexture);
            case 3 -> getPlayerTexture(player);//Minecraft.getInstance().getTextureManager().getTexture(steveTexture);
            case 4 -> Minecraft.getInstance().getTextureManager().getTexture(necoarcAtlas);
            case 5 -> Minecraft.getInstance().getTextureManager().getTexture(vibriAtlas);
            case 6 -> Minecraft.getInstance().getTextureManager().getTexture(sonicAtlas);
            default -> null;
        };
    }

    /**
     * Get the width of the texture for the given model
     * @param modelID the model ID
     * @return the width of the texture
     */
    public static float getTextureWidth(int modelID){
        return switch (modelID) {
            case 0 -> 704;
            case 1 -> 704;
            case 2 -> 320;
            case 3 -> 320;
            case 4 -> 512;
            case 5 -> 192;
            case 6 -> 512;
            default -> 64;
        };
    }

    /**
     * Get the height of the texture for the given model
     * @param modelID the model ID
     * @return the height of the texture
     */
    public static float getTextureHeight(int modelID){
        return switch (modelID) {
            default -> 64;
        };
    }

    /**
     * Get MChar texture extracted from ROM
     * @return MChar texture
     */
    public static AbstractTexture getMCharTexture() {
        return mCharTexture;
    }

    public static void setMCharTexture(NativeImage img){
        mCharTexture=new DynamicTexture(img);
        native_mChar=img;
    }

    public static AbstractTexture getLuigiTexture() {
        if (luigiTexture==null)
            luigiTexture=new DynamicTexture(TexGenerator.convertMCharTexToLuigi(native_mChar));
        return luigiTexture;
    }

    /**
     * Get player's skin as an InputStream
     * @param loc the player's skin location
     * @return the player's skin as an InputStream
     * @throws IOException if the skin cannot be found/read
     */
    public static InputStream getSkinInputStream(ResourceLocation loc) throws IOException {
        var res = Minecraft.getInstance().getResourceManager().getResource(loc);
        if (!res.isEmpty()){
            return res.get().open();
        }
        // manually find the skin
        var s = loc.getPath().substring(6);
        var skinsRoot = Minecraft.getInstance().getSkinManager().skinTextures.root;
        Path skinPath = skinsRoot.resolve(s.length() > 2 ? s.substring(0, 2) : "xx").resolve(s);
        return new FileInputStream(skinPath.toFile());
    }

    public static DynamicTexture extendSkinTexture(ResourceLocation skinLoc) throws IOException {

        var iio=getSkinInputStream(skinLoc);
        var nativeImg=NativeImage.read(iio);
        NativeImage skinAtlas = new NativeImage(320,64,true);
        for (int y = 0; y < nativeImg.getHeight(); y++) {
            for (int x = 0; x < nativeImg.getWidth(); x++) {
                skinAtlas.setPixelRGBA(x,y,nativeImg.getPixelRGBA(x,y));
            }
        }
        if (nativeImg.getHeight()==32){
            // convert texture by copying over arm/leg
            TexGenerator.overlayImage(skinAtlas,nativeImg,16,48,0,16,16,16);
            TexGenerator.overlayImage(skinAtlas,nativeImg,32,48,40,16,16,16);
        }

        skinAtlas = TexGenerator.appendSteveStuffToTex(skinAtlas);

        //skinAtlas.writeToFile(new File("skinAtlas.png"));
        return new DynamicTexture(skinAtlas);
    }

    public static DynamicTexture getSteveTexture(){
        if (defaultSkin == null) {
            try {
                defaultSkin = extendSkinTexture(DefaultPlayerSkin.getDefaultTexture());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultSkin;
    }

    /**
     * Get the texture for the given player (mapped to the Steve/Alex model)
     * @param player the player
     * @return the texture for the given player
     */
    public static DynamicTexture getPlayerTexture(Player player){
        var pname=player.getName().getString();
        if (textureMap.containsKey(pname)){
            if (textureMap.get(pname)==null){
                return getSteveTexture();
            }else{
                return textureMap.get(pname);
            }
        }else{
            textureMap.put(pname,null);

            // download texture
            var gameProfile= player.getGameProfile();
            Minecraft.getInstance().getSkinManager().getOrLoad(gameProfile).thenApply(PlayerSkin::texture).thenAccept((skin)->{
                try {
                    var etex=extendSkinTexture(skin);
                    if (etex==null)
                        etex=getSteveTexture();
                    textureMap.put(pname,etex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return getSteveTexture();
        }
    }

}
