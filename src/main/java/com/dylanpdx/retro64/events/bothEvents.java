package com.dylanpdx.retro64.events;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.networking.Retro64Net;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Events for both the client and server side
 */
public class bothEvents {

    @SubscribeEvent
    public void playerSize(EntityEvent.Size event){
        if (event.getEntity() instanceof Player){
            if (RemoteMCharHandler.getIsMChar((Player) event.getEntity())){
                event.setNewSize(new EntityDimensions(.5f,1.3f,1f,event.getEntity().getAttachments(),true));
            }
        }
    }

    @SubscribeEvent
    public void RegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        Retro64Net.register(event);
    }

}
