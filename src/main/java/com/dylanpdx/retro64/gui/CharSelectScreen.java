package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.ModelData;
import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

// import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CharSelectScreen extends Screen {
    // private Component deathScore;
    // private final List<Button> exitButtons = Lists.newArrayList();

    public CharSelectScreen() {
        super(Component.nullToEmpty("charSelect"));

    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

    public boolean isPauseScreen() {
        return false;
    }

    protected void init(){

        this.addRenderableWidget(Button.builder(Component.translatable("charSelect.retro64.prev"), new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                SM64EnvManager.playerModel--;
                if (SM64EnvManager.playerModel<0){
                    SM64EnvManager.playerModel=ModelData.modelCount-1;
                }
            }
        }).pos(10, this.height-40).size(50,20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("charSelect.retro64.next"), new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                SM64EnvManager.playerModel++;
                if (SM64EnvManager.playerModel>=ModelData.modelCount){
                    SM64EnvManager.playerModel=0;
                }
            }
        }).pos(this.width-60, this.height-40).size(50,20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var pPoseStack = guiGraphics.pose();
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0xA0000000, 0xF0000000);
        drawCharName(pPoseStack);
        pPoseStack.pushPose();
        pPoseStack.translate(this.width/2f,this.height/2f+30,0);
        pPoseStack.scale(-90,90,90);
        pPoseStack.mulPose(Utils.quaternionFromXYZ((float)Math.toRadians(180),0,(float)Math.toRadians(0)));
        minecraft.getEntityRenderDispatcher().render(minecraft.player, 0,0,0,0,1,pPoseStack,this.minecraft.renderBuffers().bufferSource(), 15728880);
        pPoseStack.popPose();
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    void drawCharName(PoseStack pPoseStack){
        var dat = Component.literal((ModelData.values()[SM64EnvManager.playerModel]).getName());
        var creditDat = Component.nullToEmpty(""+(ModelData.values()[SM64EnvManager.playerModel]).getCredit());
        var fWidth = this.font.width(dat);
        var cWidth = this.font.width(creditDat);
        this.font.draw(pPoseStack, dat, (this.width / 2f)-(fWidth/2f), this.height-40, 0xFFFFFF);
        this.font.draw(pPoseStack, creditDat, (this.width / 2f)-(cWidth/2f), this.height-60, 0x999999);
    }

}
