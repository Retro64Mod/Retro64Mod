package com.dylanpdx.retro64;

// import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
// import java.util.UUID;

public class textureManager {

    static HashMap<String, DynamicTexture> textureMap = new HashMap<String, DynamicTexture>();
    static DynamicTexture defaultSkin=null;

    static NativeImage native_mChar;
    static DynamicTexture mCharTexture;
    static DynamicTexture luigiTexture;
    static ResourceLocation luigiAtlas = new ResourceLocation(Retro64.MOD_ID,"textures/model/luigi_atlas.png");
    static ResourceLocation steveAtlas = new ResourceLocation(Retro64.MOD_ID,"textures/model/steve.png");
    //static ResourceLocation steveTexture = new ResourceLocation(Retro64.MOD_ID,"textures/model/steve.png");
    //static GameProfile dpf = new GameProfile(UUID.fromString("d949016b-245f-4762-9d9a-59d972bc694e"),"SuperMChar");


    public static AbstractTexture getTextureForModel(int modelID, Player player){
        return switch (modelID) {
            case 0 -> getMCharTexture();
            case 1 -> getLuigiTexture();
            case 2 -> getPlayerTexture(player);//Minecraft.getInstance().getTextureManager().getTexture(steveTexture);
            case 3 -> getPlayerTexture(player);//Minecraft.getInstance().getTextureManager().getTexture(steveTexture);
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
            default -> 1;
        };
    }

    /**
     * Get the height of the texture for the given model
     * @param modelID the model ID
     * @return the height of the texture
     */
    public static float getTextureHeight(int modelID){
        return switch (modelID) {
            case 0 -> 64;
            case 1 -> 64;
            case 2 -> 64;
            case 3 -> 64;
            default -> 1;
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
        if (loc==DefaultPlayerSkin.getDefaultSkin())
            return Minecraft.getInstance().getResourceManager().getResource(DefaultPlayerSkin.getDefaultSkin()).getInputStream();
        // Getting it like this because the skin manager method returns a null value
        var locStr=loc.toString().replace("minecraft:skins/","");
        var first2chars=locStr.substring(0,2);
        var skinFolder=ObfuscationReflectionHelper.getPrivateValue(SkinManager.class,Minecraft.getInstance().getSkinManager(),mappingsConvert.skinsDirectory);;
        var trueSkinPath = Paths.get(skinFolder.toString(),first2chars,locStr);

        return new FileInputStream(trueSkinPath.toFile());
    }

    public static DynamicTexture extendSkinTexture(ResourceLocation skinLoc) throws IOException {

        var iio=getSkinInputStream(skinLoc);
        var nativeImg=NativeImage.read(iio);
        if (nativeImg.getHeight()!=64)
            return null;
        NativeImage skinAtlas = new NativeImage(320,64,true);
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                skinAtlas.setPixelRGBA(j,i,nativeImg.getPixelRGBA(j,i));
            }
        }
        skinAtlas = TexGenerator.appendSteveStuffToTex(skinAtlas);

        //skinAtlas.writeToFile(new File("skinAtlas.png"));
        return new DynamicTexture(skinAtlas);
    }

    public static DynamicTexture getSteveTexture(){
        if (defaultSkin == null) {
            try {
                defaultSkin = extendSkinTexture(DefaultPlayerSkin.getDefaultSkin());
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
            Minecraft.getInstance().getSkinManager().registerSkins(gameProfile, new SkinManager.SkinTextureCallback() {
                @Override
                public void onSkinTextureAvailable(MinecraftProfileTexture.Type p_118857_, ResourceLocation p_118858_, MinecraftProfileTexture p_118859_) {
                    try {
                        var etex=extendSkinTexture(p_118858_);
                        if (etex==null)
                            etex=getSteveTexture();
                        textureMap.put(pname,etex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },false);
            return getSteveTexture();
        }
    }

}
