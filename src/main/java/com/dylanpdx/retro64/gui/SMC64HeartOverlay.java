package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.RemoteMCharHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class SMC64HeartOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float v, int i, int i1) {
        if (!RemoteMCharHandler.getIsMChar(Minecraft.getInstance().player) || SM64EnvManager.selfMChar.state==null || Minecraft.getInstance().options.hideGui)
            return;
        if (Minecraft.getInstance().player.isUnderWater())
            return; // will be handled by MC's default air overlay
        var healthSlices = (SM64EnvManager.selfMChar.state.health&0xff00)>>8;
        //RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);
        float healthMax=8.0f;
        int absorb=0;
        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        int left_height = 40;

        int left = guiGraphics.guiWidth() / 2 - 91;
        int top = guiGraphics.guiHeight() - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;
        //renderHearts(gui,mStack, Minecraft.getInstance().player,left,top,11,-1,healthMax,healthSlices,8,absorb,false);
        Minecraft.getInstance().gui.renderHearts(guiGraphics,Minecraft.getInstance().player,left,top,11,-1,healthMax,healthSlices,8,absorb,false);
    }
}
