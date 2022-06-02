package com.dylanpdx.retro64.events;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.capabilities.sm64CapabilityAttacher;
import com.dylanpdx.retro64.capabilities.smc64Capability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Events for both the client and server side
 */
public class bothEvents {
    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event){
        smc64Capability.register(event);

    }

    @SubscribeEvent
    public void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof Player){
            sm64CapabilityAttacher.attach(event);
        }
    }

    @SubscribeEvent
    public void playerSize(EntityEvent.Size event){
        if (event.getEntity() instanceof Player){
            if (RemoteMCharHandler.getIsMChar((Player) event.getEntity())){
                event.setNewSize(new EntityDimensions(.5f,1.3f,true)); // width, height, fixed
                event.setNewEyeHeight(1f);
            }
        }
    }

}
