package com.dylanpdx.retro64.mixin;

import com.dylanpdx.retro64.RemoteMCharHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

import static com.dylanpdx.retro64.mappingsConvert.m_tryCheckInsideBlocks;

@Mixin(net.minecraft.client.gui.Gui.class)
public class MixinRenderHealth {
    @Inject(at=@At("HEAD"),method="Lnet/minecraft/client/gui/Gui;renderHealthLevel(Lnet/minecraft/client/gui/GuiGraphics;)V", cancellable = true)
    private void renderHealthLevel(GuiGraphics p_283143_, CallbackInfo ci){
        var gui = ((net.minecraft.client.gui.Gui)(Object)this);
        var player = Minecraft.getInstance().player;
        if (RemoteMCharHandler.getIsMChar(player)){
            ci.cancel();
        }
    }
}
