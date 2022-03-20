package com.dylanpdx.retro64.gui;

import com.dylanpdx.retro64.Retro64;
import com.dylanpdx.retro64.sm64.libsm64.Libsm64Library;
import com.dylanpdx.retro64.sm64.libsm64.SM64Sounds;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
// import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
// import net.minecraftforge.client.gui.ForgeIngameGui;
// import net.minecraftforge.client.gui.IIngameOverlay;

public class SMC64DialogOverlay extends Screen {

    static final int DIALOG_STATE_OPENING = 0;
    static final int DIALOG_STATE_VERTICAL = 1;
    static final int DIALOG_STATE_HORIZONTAL = 2;
    static final int DIALOG_STATE_CLOSING = 3;
    static final int DIALOG_TYPE_ROTATE = 0;
    static final int DIALOG_TYPE_ZOOM = 1;
    static final float DEFAULT_DIALOG_BOX_ANGLE=90.0f;
    static final float div=3;
    static final int DIAG_VAL1=16;
    static final int DIAG_VAL2=240;
    static final int DIAG_VAL3=132;
    static final int DIAG_VAL4=5;

    public static final ResourceLocation dialogBgLoc = new ResourceLocation(Retro64.MOD_ID,"textures/ui/dialog.png");
    static float gDialogBoxScale = 19.0f;
    static float gDialogBoxOpenTimer = 90.0f;
    static int gDialogBoxState = DIALOG_STATE_OPENING;
    static int gDialogBoxType = DIALOG_TYPE_ROTATE;
    short gLastDialogPageStrPos = -1;
    short gDialogTextPos = 0;
    short gDialogScrollOffsetY = 0;
    short gDialogLineNum = 1;
    int linesPerBox = 4;
    boolean advanceDialogKey=false;


    public SMC64DialogOverlay(Component pTitle) {
        super(pTitle);
        gDialogBoxState = DIALOG_STATE_OPENING;
        gDialogBoxOpenTimer = 90.0f;
        gDialogBoxScale = 19.0f;
        gDialogBoxType=DIALOG_TYPE_ROTATE;
    }
    //gDialogID = DIALOG_NONE;
    //gDialogTextPos = 0;
    //gLastDialogResponse = 0;
    //gLastDialogPageStrPos = 0;
    //gDialogResponse = DIALOG_RESPONSE_NONE;

    void create_dl_rotation_matrix(PoseStack mStack, float angle, float x, float y, float z) {
        Quaternion q = new Quaternion(new Vector3f(0,0,1),angle,true);
        mStack.mulPose(q);
    }

    void create_dl_translation_matrix(PoseStack mStack, float x, float y, float z) {
        mStack.translate(x, y, z);
    }

    void create_dl_scale_matrix(PoseStack mStack, float x, float y, float z) {
        mStack.scale(x, y, z);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        tickDialog();
        RenderSystem.setShaderTexture(0,dialogBgLoc);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        pPoseStack.pushPose();
        create_dl_translation_matrix(pPoseStack,70,20,0);
        switch (gDialogBoxType) {
            case DIALOG_TYPE_ROTATE: // Renders a dialog black box with zoom and rotation
                if (gDialogBoxState == DIALOG_STATE_OPENING || gDialogBoxState == DIALOG_STATE_CLOSING) {
                    create_dl_scale_matrix(pPoseStack, 1.0f / gDialogBoxScale, 1.0f / gDialogBoxScale, 1.0f);
                    // convert the speed into angle
                    create_dl_rotation_matrix(pPoseStack, gDialogBoxOpenTimer * 4.0f, 0, 0, 1.0f);
                }
                //gDPSetEnvColor(gDisplayListHead++, 0, 0, 0, 150);
                break;
            case DIALOG_TYPE_ZOOM: // Renders a dialog white box with zoom
                if (gDialogBoxState == DIALOG_STATE_OPENING || gDialogBoxState == DIALOG_STATE_CLOSING) {
                    create_dl_translation_matrix(pPoseStack, 65.0f - (65.0f / gDialogBoxScale),
                            (40.0f / gDialogBoxScale) - 40, 0);
                    create_dl_scale_matrix(pPoseStack, 1.0f / gDialogBoxScale, 1.0f / gDialogBoxScale, 1.0f);
                }
                //gDPSetEnvColor(gDisplayListHead++, 255, 255, 255, 150);
                break;
        }


        this.fillGradient(pPoseStack, 0, 0, 65*2, 40*2, 0x96000000, 0x96000000);
        var splitTxt = this.font.split(FormattedText.of("test dialog"),65*2);
        for (int i = 0; i < splitTxt.size(); i++) {
            this.font.draw(pPoseStack, splitTxt.get(i), 2, 2+(i*10), 0xFFFFFF);
        }
        pPoseStack.popPose();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == InputConstants.KEY_SPACE) {
            advanceDialogKey=true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public void tickDialog(){
        // https://github.com/n64decomp/sm64/blob/1372ae1bb7cbedc03df366393188f4f05dcfc422/src/game/ingame_menu.c#L1716
        switch (gDialogBoxState) {
            case DIALOG_STATE_OPENING:
                if (gDialogBoxOpenTimer >= DEFAULT_DIALOG_BOX_ANGLE) {
                    //play_dialog_sound(gDialogID);
                    Libsm64Library.INSTANCE.sm64_play_sound_global(SM64Sounds.SOUND_MENU_MESSAGE_APPEAR.getBits());
                    //play_sound(SOUND_MENU_MESSAGE_APPEAR, gGlobalSoundSource);
                }

                if (gDialogBoxType == DIALOG_TYPE_ROTATE) {
                    gDialogBoxOpenTimer -= 7.5/div;
                    gDialogBoxScale -= 1.5/div;
                } else {
                    gDialogBoxOpenTimer -= 10.0/div;
                    gDialogBoxScale -= 2.0/div;
                }

                if (gDialogBoxOpenTimer == 0.0f) {
                    gDialogBoxState = DIALOG_STATE_VERTICAL;
                    //gDialogLineNum = 1;
                }
                break;
            case DIALOG_STATE_VERTICAL:
                gDialogBoxOpenTimer = 0.0f;

                if (advanceDialogKey) {
                    advanceDialogKey=false;
                    if (gLastDialogPageStrPos == -1) {
                        //handle_special_dialog_text(gDialogID);
                        gDialogBoxState = DIALOG_STATE_CLOSING;
                    } else {
                        gDialogBoxState = DIALOG_STATE_HORIZONTAL;
                        //play_sound(SOUND_MENU_MESSAGE_NEXT_PAGE, gGlobalSoundSource);
                        Libsm64Library.INSTANCE.sm64_play_sound_global(SM64Sounds.SOUND_MENU_MESSAGE_NEXT_PAGE.getBits());
                    }
                }
                break;
            case DIALOG_STATE_HORIZONTAL:
                gDialogScrollOffsetY += linesPerBox * 2;

                if (gDialogScrollOffsetY >= linesPerBox * DIAG_VAL1) {
                    gDialogTextPos = gLastDialogPageStrPos;
                    gDialogBoxState = DIALOG_STATE_VERTICAL;
                    gDialogScrollOffsetY = 0;
                }
                break;
            case DIALOG_STATE_CLOSING:
                if (gDialogBoxOpenTimer == 20.0f) {
                    //level_set_transition(0, NULL);
                    //play_sound(SOUND_MENU_MESSAGE_DISAPPEAR, gGlobalSoundSource);
                    Libsm64Library.INSTANCE.sm64_play_sound_global(SM64Sounds.SOUND_MENU_MESSAGE_DISAPPEAR.getBits());

                    if (gDialogBoxType == DIALOG_TYPE_ZOOM) {
                        //trigger_cutscene_dialog(2);
                    }

                    //gDialogResponse = gDialogLineNum;
                }

                gDialogBoxOpenTimer = gDialogBoxOpenTimer + 10.0f/div;
                gDialogBoxScale = gDialogBoxScale + 2.0f/div;

                if (gDialogBoxOpenTimer >= DEFAULT_DIALOG_BOX_ANGLE) {
                    gDialogBoxState = DIALOG_STATE_OPENING;
                    Minecraft.getInstance().setScreen(null);
                    /*gDialogID = DIALOG_NONE;
                    gDialogTextPos = 0;
                    gLastDialogResponse = 0;
                    gLastDialogPageStrPos = 0;
                    gDialogResponse = DIALOG_RESPONSE_NONE;*/
                }
        }
    }
}
