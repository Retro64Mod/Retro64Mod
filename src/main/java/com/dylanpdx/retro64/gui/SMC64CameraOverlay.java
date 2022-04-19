package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.textureManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SMC64CameraOverlay implements IIngameOverlay {

    // https://github.com/n64decomp/sm64/blob/1372ae1bb7cbedc03df366393188f4f05dcfc422/src/game/camera.h#L161-L169
    private final int CAM_STATUS_NONE  = 0;
    private final int CAM_STATUS_MARIO = 1 << 0;
    private final int CAM_STATUS_LAKITU =1 << 1;
    private final int CAM_STATUS_FIXED = 1 << 2;
    private final int CAM_STATUS_C_DOWN= 1 << 3;
    private final int CAM_STATUS_C_UP  = 1 << 4;
    private final int CAM_STATUS_MODE_GROUP =  (CAM_STATUS_MARIO | CAM_STATUS_LAKITU | CAM_STATUS_FIXED);
    private final int CAM_STATUS_C_MODE_GROUP =(CAM_STATUS_C_DOWN | CAM_STATUS_C_UP);

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        poseStack.pushPose();
        int left = width / 2 + 50;
        int top = height - 50;
        poseStack.translate(left,top,0);
        poseStack.scale(.05f,.05f,.05f);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderTexture(0, textureManager.cameraIcon);
        gui.blit(poseStack, 0 , 0 , 0, 0, 255, 255);

        switch (SM64EnvManager.selfMChar.state.cameraStatus & CAM_STATUS_MODE_GROUP) {
            case CAM_STATUS_MARIO:
                // draw mario icon
                break;
            case CAM_STATUS_LAKITU:
                RenderSystem.setShaderTexture(0, textureManager.cameraViewIcon);
                gui.blit(poseStack, 255 , 0 , 0, 0, 255, 255);
                break;
            case CAM_STATUS_FIXED:
                RenderSystem.setShaderTexture(0, textureManager.cameraXIcon);
                gui.blit(poseStack, 255 , 0 , 0, 0, 255, 255);
                break;
        }

        switch (SM64EnvManager.selfMChar.state.cameraStatus & CAM_STATUS_C_MODE_GROUP) {
            case CAM_STATUS_C_DOWN:
                RenderSystem.setShaderTexture(0, textureManager.cameraDNIcon);
                gui.blit(poseStack, 0 , 255 , 0, 0, 255, 255);
                break;
            case CAM_STATUS_C_UP:
                RenderSystem.setShaderTexture(0, textureManager.cameraUPIcon);
                gui.blit(poseStack, 0 , -255 , 0, 0, 255, 255);
                break;
        }



        poseStack.popPose();
    }
}
