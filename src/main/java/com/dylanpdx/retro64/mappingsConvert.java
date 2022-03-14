package com.dylanpdx.retro64;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public class mappingsConvert {

    public static final String skinsDirectory = "f_118808_";
    public static final String renderNameTag = "m_7649_";
    public static final String MINECRAFT_LOGO = "f_96723_";
    public static final String MINECRAFT_EDITION = "f_96724_";
    public static final String tryCheckInsideBlocks = "m_146872_";
    private static final Method m_renderNameTag = ObfuscationReflectionHelper.findMethod(PlayerRenderer.class,mappingsConvert.renderNameTag,
            AbstractClientPlayer.class, Component.class, PoseStack.class, MultiBufferSource.class,int.class);

    public static final Method m_tryCheckInsideBlocks = ObfuscationReflectionHelper.findMethod(Entity.class,mappingsConvert.tryCheckInsideBlocks);

    public static void renderNameTag(PlayerRenderer pr, AbstractClientPlayer player,Component pDisplayName,PoseStack pMatrixStack,MultiBufferSource pBuffer,int pPackedLight) {
        try {
            m_renderNameTag.invoke(pr,player,pDisplayName,pMatrixStack,pBuffer,pPackedLight);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
