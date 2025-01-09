package com.dylanpdx.retro64.mixin;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.sm64.libsm64.LibSM64;
import com.dylanpdx.retro64.sm64.libsm64.Libsm64Library;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

import static com.dylanpdx.retro64.mappingsConvert.m_tryCheckInsideBlocks;

@Mixin(Entity.class)
public class MixinEntityMove {
    @Inject(at=@At("HEAD"),method="Lnet/minecraft/world/entity/Entity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",remap = true)
    private void plrTravel(MoverType type, Vec3 movement, CallbackInfo ci){
        var thisEnt = ((Entity)(Object)this);
        if (thisEnt instanceof LocalPlayer){
            if (SM64EnvManager.selfMChar != null){
                var pos = SM64EnvManager.selfMChar.position();
                pos.add(movement.toVector3f());
                SM64EnvManager.selfMChar.teleport(pos);
            }
        }
    }
}
