package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.ModelData;
import com.dylanpdx.retro64.SM64EnvManager;
// import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
        this.addRenderableWidget(new Button(10, this.height-40, 50, 20,Component.translatable("charSelect.retro64.prev"), (pButton) -> {
            SM64EnvManager.playerModel--;
            if (SM64EnvManager.playerModel<0){
                SM64EnvManager.playerModel=ModelData.modelCount-1;
            }
        }));
        this.addRenderableWidget(new Button(this.width-60, this.height-40, 50, 20,Component.translatable("charSelect.retro64.next"), (pButton) -> {
            SM64EnvManager.playerModel++;
            if (SM64EnvManager.playerModel>=ModelData.modelCount){
                SM64EnvManager.playerModel=0;
            }
        }));
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.fillGradient(pPoseStack, 0, 0, this.width, this.height, 0xA0000000, 0xF0000000);
        drawCharName(pPoseStack);
        pPoseStack.pushPose();
        pPoseStack.translate(this.width/2f,this.height/2f+30,0);
        pPoseStack.scale(-90,90,90);
        pPoseStack.mulPose(Quaternion.fromXYZ((float)Math.toRadians(180),0,(float)Math.toRadians(0)));
        minecraft.getEntityRenderDispatcher().render(minecraft.player, 0,0,0,0,1,pPoseStack,this.minecraft.renderBuffers().bufferSource(), 15728880);
        pPoseStack.popPose();
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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
