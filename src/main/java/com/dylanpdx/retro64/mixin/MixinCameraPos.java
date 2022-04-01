package com.dylanpdx.retro64.mixin;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.Utils;
import com.dylanpdx.retro64.mappingsConvert;
import com.dylanpdx.retro64.sm64.libsm64.LibSM64;
import com.dylanpdx.retro64.sm64.libsm64.Libsm64Library;
import com.dylanpdx.retro64.sm64.libsm64.PUFixer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(Camera.class)
public class MixinCameraPos {

    //
    @Inject(at=@At(value="INVOKE",target="Lnet/minecraft/client/Camera;setRotation(FF)V"), method="Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", cancellable = true)
    private void setup(BlockGetter pLevel, Entity pEntity, boolean pDetached, boolean pThirdPersonReverse, float pPartialTick, CallbackInfo callback){
        if (RemoteMCharHandler.getIsMChar(Minecraft.getInstance().player) && pDetached)
        {
            var thisCam = ((Camera)(Object)this);
            try {
                var camPos = SM64EnvManager.selfMChar.state.camPos;
                var camLook = SM64EnvManager.selfMChar.state.camFocus;
                var camPosV3 = new Vector3f(camPos[0], camPos[1], camPos[2]);
                var camLookV3 = new Vector3f(camLook[0], camLook[1], camLook[2]);
                camPosV3 = PUFixer.convertToMC(camPosV3);
                camLookV3 = PUFixer.convertToMC(camLookV3);
                mappingsConvert.m_cameraSetPosition.invoke(thisCam, camPosV3.x(), camPosV3.y(), camPosV3.z());
                //var lookRot = Utils.QuaternionLookRotation(camLookV3,thisCam.getUpVector()).toXYZ();
                camLookV3.normalize();
                ObfuscationReflectionHelper.findField(Camera.class,mappingsConvert.camera_forwards).set(thisCam,camLookV3);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            callback.cancel();
        }
    }
}

