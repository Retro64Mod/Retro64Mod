package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.RemoteMCharHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class SMC64HeartOverlay implements LayeredDraw.Layer {
    Random random = new Random();
    Method forPlayerMethod=null;

    private void renderHearts(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight) throws InvocationTargetException, IllegalAccessException {
        if (forPlayerMethod==null)
            forPlayerMethod = ObfuscationReflectionHelper.findMethod(Gui.HeartType.class,"forPlayer",Player.class);
        if (Minecraft.getInstance().options.hideGui)
            return;
        Gui.HeartType gui$hearttype = (Gui.HeartType) forPlayerMethod.invoke(null,player);
        boolean flag = player.level().getLevelData().isHardcore();
        int i = Mth.ceil((double)maxHealth / 2.0);
        int j = Mth.ceil((double)absorptionAmount / 2.0);
        int k = i * 2;

        for(int l = i + j - 1; l >= 0; --l) {
            int i1 = l / 10;
            int j1 = l % 10;
            int k1 = x + j1 * 8;
            int l1 = y - i1 * height;
            if (currentHealth + absorptionAmount <= 4) {
                l1 += this.random.nextInt(2);
            }

            if (l < i && l == offsetHeartIndex) {
                l1 -= 2;
            }

            this.renderHeart(guiGraphics, Gui.HeartType.CONTAINER, k1, l1, flag, renderHighlight, false);
            int i2 = l * 2;
            boolean flag1 = l >= i;
            if (flag1) {
                int j2 = i2 - k;
                if (j2 < absorptionAmount) {
                    boolean flag2 = j2 + 1 == absorptionAmount;
                    this.renderHeart(guiGraphics, gui$hearttype == Gui.HeartType.WITHERED ? gui$hearttype : Gui.HeartType.ABSORBING, k1, l1, flag, false, flag2);
                }
            }

            boolean flag4;
            if (renderHighlight && i2 < displayHealth) {
                flag4 = i2 + 1 == displayHealth;
                this.renderHeart(guiGraphics, gui$hearttype, k1, l1, flag, true, flag4);
            }

            if (i2 < currentHealth) {
                flag4 = i2 + 1 == currentHealth;
                this.renderHeart(guiGraphics, gui$hearttype, k1, l1, flag, false, flag4);
            }
        }

    }

    private void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking) {
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(heartType.getSprite(hardcore, blinking, halfHeart), x, y, 9, 9);
        RenderSystem.disableBlend();
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!RemoteMCharHandler.getIsMChar(Minecraft.getInstance().player) || SM64EnvManager.selfMChar.state==null)
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
        try {
            renderHearts(guiGraphics,Minecraft.getInstance().player,left,top,11,-1,healthMax,healthSlices,8,absorb,false);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
