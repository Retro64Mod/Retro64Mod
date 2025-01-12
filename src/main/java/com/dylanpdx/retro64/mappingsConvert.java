package com.dylanpdx.retro64;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class mappingsConvert {

    public static final String tryCheckInsideBlocks = "tryCheckInsideBlocks";

    public static final Method m_tryCheckInsideBlocks = ObfuscationReflectionHelper.findMethod(Entity.class,mappingsConvert.tryCheckInsideBlocks);

}
