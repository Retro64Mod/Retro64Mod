package com.dylanpdx.retro64.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public class LibLoadWarnScreen extends Screen {

    private Button okButton;
    private MutableComponent reason;

    protected LibLoadWarnScreen(Component pTitle) {
        super(pTitle);
    }

    public LibLoadWarnScreen(MutableComponent reason) {
        super(Component.literal("Warning"));
        this.reason = reason;
    }

    protected void init() {
        okButton = Button.builder(Component.translatable("menu.retro64.ok"),pButton -> {Minecraft.getInstance().setScreen(new TitleScreen());})
                .pos(this.width / 2 - 100, this.height / 2 + 20)
                .size(200, 20).build();
        this.addRenderableWidget(okButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        //drawTextCentered(pPoseStack, title, width / 2, 20, 0xFFFFFF);
        //drawTextCenteredWrapped(pPoseStack,reason, width / 2, 40, 300,0xFFFFFF);
        //drawTextCenteredWrapped(pPoseStack,Component.translatable("menu.retro64.genericWarn"), width / 2, 60, 300,0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    void drawTextCentered(PoseStack pPoseStack, Component pText, int pX, int pY, int pColor) {
        var lSize = font.width(pText);
        //font.draw(pPoseStack, pText, pX - lSize / 2f, pY, pColor);
    }

    void drawTextCentered(PoseStack pPoseStack, FormattedCharSequence pText, int pX, int pY, int pColor) {
        var lSize = font.width(pText);
        //font.draw(pPoseStack, pText, pX - lSize / 2f, pY, pColor);
    }

    void drawTextCenteredWrapped(PoseStack pPoseStack, Component pText, int pX, int pY, int pWidth, int pColor) {
        var wrapped = font.split(pText, pWidth);
        int cLine=0;
        for (var line : wrapped) {
            drawTextCentered(pPoseStack, line, pX, pY +(10*cLine), pColor);
            cLine++;
        }
    }

    void drawTextCenteredWrapped(PoseStack pPoseStack, String pText, int pX, int pY, int pWidth, int pColor) {
        drawTextCenteredWrapped(pPoseStack, Component.literal(pText), pX, pY, pWidth, pColor);
    }
}
