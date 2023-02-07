package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.RemoteMCharHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Random;

public class SMC64HeartOverlay implements IGuiOverlay {

    Random random = new Random();

    @Override
    public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if (!RemoteMCharHandler.getIsMChar(Minecraft.getInstance().player) || SM64EnvManager.selfMChar.state==null)
            return;
        if (Minecraft.getInstance().player.isUnderWater())
            return; // will be handled by MC's default air overlay
        var healthSlices = (SM64EnvManager.selfMChar.state.health&0xff00)>>8;
        RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);
        float healthMax=8.0f;
        int absorb=0;
        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        int left_height = 40;
        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) left_height += 10 - rowHeight;
        renderHearts(gui,mStack, Minecraft.getInstance().player,left,top,11,-1,healthMax,healthSlices,8,absorb,false);
    }

    protected void renderHearts(ForgeGui gui,PoseStack poseStack, Player thePlayer, int left, int top, int rowHeight, int regen, float healthMax, int health, int healthLast, int absorb, boolean highlight) {
        HeartType gui$hearttype = HeartType.NORMAL;
        int i = 9 * (thePlayer.level.getLevelData().isHardcore() ? 5 : 0);
        int j = Mth.ceil((double)healthMax / 2.0D);
        int k = Mth.ceil((double)absorb / 2.0D);
        int l = j * 2;

        for(int i1 = j + k - 1; i1 >= 0; --i1) {
            int j1 = i1 / 10;
            int k1 = i1 % 10;
            int l1 = left + k1 * 8;
            int i2 = top - j1 * rowHeight;
            if (health + absorb <= 2) {
                i2 += this.random.nextInt(2);
            }

            if (i1 < j && i1 == regen) {
                i2 -= 2;
            }

            this.renderHeart(gui,poseStack, HeartType.CONTAINER, l1, i2, i, highlight, false);
            int j2 = i1 * 2;
            boolean flag = i1 >= j;
            if (flag) {
                int k2 = j2 - l;
                if (k2 < absorb) {
                    boolean flag1 = k2 + 1 == absorb;
                    this.renderHeart(gui,poseStack, gui$hearttype == HeartType.WITHERED ? gui$hearttype : HeartType.ABSORBING, l1, i2, i, false, flag1);
                }
            }

            if (highlight && j2 < healthLast) {
                boolean flag2 = j2 + 1 == healthLast;
                this.renderHeart(gui,poseStack, gui$hearttype, l1, i2, i, true, flag2);
            }

            if (j2 < health) {
                boolean flag3 = j2 + 1 == health;
                this.renderHeart(gui,poseStack, gui$hearttype, l1, i2, i, false, flag3);
            }
        }

    }

    private void renderHeart(ForgeGui gui,PoseStack p_168701_, HeartType p_168702_, int p_168703_, int p_168704_, int p_168705_, boolean p_168706_, boolean p_168707_) {
        gui.blit(p_168701_, p_168703_, p_168704_, p_168702_.getX(p_168707_, p_168706_), p_168705_, 9, 9);
    }

    @OnlyIn(Dist.CLIENT)
    static enum HeartType {
        CONTAINER(0, false),
        NORMAL(2, true),
        POISIONED(4, true),
        WITHERED(6, true),
        ABSORBING(8, false),
        FROZEN(9, false);

        private final int index;
        private final boolean canBlink;

        private HeartType(int p_168729_, boolean p_168730_) {
            this.index = p_168729_;
            this.canBlink = p_168730_;
        }

        public int getX(boolean p_168735_, boolean p_168736_) {
            int i;
            if (this == CONTAINER) {
                i = p_168736_ ? 1 : 0;
            } else {
                int j = p_168735_ ? 1 : 0;
                int k = this.canBlink && p_168736_ ? 2 : 0;
                i = j + k;
            }

            return 16 + (this.index * 2 + i) * 9;
        }

        static HeartType forPlayer(Player pPlayer) {
            HeartType gui$hearttype;
            if (pPlayer.hasEffect(MobEffects.POISON)) {
                gui$hearttype = POISIONED;
            } else if (pPlayer.hasEffect(MobEffects.WITHER)) {
                gui$hearttype = WITHERED;
            } else if (pPlayer.isFullyFrozen()) {
                gui$hearttype = FROZEN;
            } else {
                gui$hearttype = NORMAL;
            }

            return gui$hearttype;
        }
    }
}
