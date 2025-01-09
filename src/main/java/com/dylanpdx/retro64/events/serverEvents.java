package com.dylanpdx.retro64.events;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.capabilities.capabilitySyncManager;
import com.dylanpdx.retro64.networking.SM64PacketHandler;
import com.dylanpdx.retro64.networking.damagePacket;
import com.dylanpdx.retro64.networking.healPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class serverEvents {

    @SubscribeEvent
    public static void playerAttack(LivingAttackEvent event){
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

            SM64PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),new damagePacket(pos,1));
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.StartTracking event){
        if (event.getEntity() instanceof ServerPlayer && event.getTarget() instanceof Player){
            var plr = (Player)event.getTarget();
            capabilitySyncManager.syncServerToClient(plr);
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupXP(PlayerXpEvent.XpChange event){
        if (RemoteMCharHandler.getIsMChar(event.getEntity())){
            SM64PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),new healPacket((byte)event.getAmount()));
        }
    }

}
