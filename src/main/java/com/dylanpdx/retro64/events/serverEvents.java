package com.dylanpdx.retro64.events;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.networking.packets.DamagePacket;
import com.dylanpdx.retro64.networking.packets.HealPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class serverEvents {

    @SubscribeEvent
    public void playerAttack(LivingIncomingDamageEvent event){
        if (event.getEntity() instanceof ServerPlayer){
            if (!RemoteMCharHandler.getIsMChar((Player) event.getEntity()) || event.getSource().getMsgId().equals("outOfWorld"))
                return;
            event.setCanceled(true); // cancel the event, so we can handle it ourselves
            // cancel certain types of damage so it's handled by SM64
            if (event.getEntity().hurtMarked ||
                    event.getSource().getMsgId().equals("inWall") ||
                    event.getSource().getMsgId().equals("lava") ||
                    event.getSource().getMsgId().equals("drown") ||
                    event.getSource().getMsgId().equals("inFire") ||
                    event.getSource().getMsgId().equals("magic")) // poison?
                return;
            event.getEntity().hurtMarked=true;
            Vec3 pos = event.getEntity().position().add(event.getEntity().getForward());
            if (event.getSource().getSourcePosition()!=null) // if the source has a position, use that
                pos = event.getSource().getSourcePosition();

            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new DamagePacket(pos.toVector3f(),(byte)1));
        }
    }

    /*@SubscribeEvent
    public void onPlayerJoin(PlayerEvent.StartTracking event){
        if (event.getEntity() instanceof ServerPlayer && event.getTarget() instanceof Player){
            var plr = (Player)event.getTarget();
            capabilitySyncManager.syncServerToClient(plr);
        }
    }*/

    @SubscribeEvent
    public void onPlayerPickupXP(PlayerXpEvent.XpChange event){
        if (RemoteMCharHandler.getIsMChar(event.getEntity())){
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new HealPacket((byte)event.getAmount()));
        }
    }

}
