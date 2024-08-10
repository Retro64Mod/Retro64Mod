package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.Retro64;
import com.dylanpdx.retro64.sm64.libsm64.PUFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SM64PacketHandler {

    /*private static int cid=0;
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Retro64.MOD_ID, "mcharpacket"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets(){
        INSTANCE.registerMessage(cid++, mCharPacket.class, mCharPacket.encoder, mCharPacket.decoder, new BiConsumer<mCharPacket, Supplier<NetworkEvent.Context>>() {
            @Override
            public void accept(mCharPacket mCharPacket, Supplier<NetworkEvent.Context> contextSupplier) {
                NetworkEvent.Context context = contextSupplier.get();
                context.enqueueWork(() -> {
                    ServerPlayer player = context.getSender();
                    player.setPos(mCharPacket.pos.x(),mCharPacket.pos.y(),mCharPacket.pos.z());
                    player.noPhysics=true;
                });
                var plr = context.getSender();
                PacketDistributor.PacketTarget targ = PacketDistributor.TRACKING_ENTITY.with(() -> plr);
                SM64PacketHandler.INSTANCE.send(targ,
                        new modelPacket(
                                mCharPacket.animInfo,
                                mCharPacket.animXRot,mCharPacket.animYRot,mCharPacket.animZRot,
                                mCharPacket.action,mCharPacket.model, PUFixer.convertToSM64(new Vec3(mCharPacket.pos.x(),mCharPacket.pos.y(),mCharPacket.pos.z())),
                                context.getSender().getUUID()
                        ));
                context.setPacketHandled(true);
            }
        });

        INSTANCE.registerMessage(cid++, modelPacket.class, modelPacket.encoder, modelPacket.decoder, new BiConsumer<modelPacket, Supplier<NetworkEvent.Context>>() {
            @Override
            public void accept(modelPacket modelPacket, Supplier<NetworkEvent.Context> contextSupplier) {
                NetworkEvent.Context context = contextSupplier.get();
                Player sender = Minecraft.getInstance().level.getPlayerByUUID(modelPacket.user);
                if (sender!=null && !sender.getUUID().equals(Minecraft.getInstance().player.getUUID()))
                {
                    RemoteMCharHandler.updateMChar(sender,modelPacket.animInfo,modelPacket.animXRot,modelPacket.animYRot,modelPacket.animZRot,modelPacket.action,modelPacket.model,modelPacket.pos);
                }
                context.setPacketHandled(true);
            }
        });

        INSTANCE.registerMessage(cid++, damagePacket.class, damagePacket.encoder, damagePacket.decoder, new BiConsumer<damagePacket, Supplier<NetworkEvent.Context>>() {
            @Override
            public void accept(damagePacket damagePacket, Supplier<NetworkEvent.Context> contextSupplier) {
                if (SM64EnvManager.selfMChar!=null)
                    SM64EnvManager.selfMChar.damage(damagePacket.damageAmount,damagePacket.pos);
                contextSupplier.get().setPacketHandled(true);
            }
        });

        INSTANCE.registerMessage(cid++, healPacket.class, healPacket.encoder, healPacket.decoder, new BiConsumer<healPacket, Supplier<NetworkEvent.Context>>() {
            @Override
            public void accept(healPacket healPck, Supplier<NetworkEvent.Context> contextSupplier) {
                if (SM64EnvManager.selfMChar!=null)
                    SM64EnvManager.selfMChar.heal(healPck.healAmount);
                contextSupplier.get().setPacketHandled(true);
            }
        });

        INSTANCE.registerMessage(cid++,capabilityPacket.class, capabilityPacket.encoder, capabilityPacket.decoder, new BiConsumer<capabilityPacket, Supplier<NetworkEvent.Context>>() {
            @Override
            public void accept(capabilityPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
                var dir = contextSupplier.get().getDirection();
                if (dir == NetworkDirection.PLAY_TO_CLIENT){ // server -> all clients
                    capabilitySyncManager.handleRecvFromServer(packet.capability,packet.getPlayer());
                }else if (dir == NetworkDirection.PLAY_TO_SERVER) { // 1 client -> server
                    if (!packet.capability.getIsEnabled() && packet.kill && contextSupplier.get().getSender().gameMode.isSurvival())
                        contextSupplier.get().getSender().kill(); // only kill if the capability is disabled and the player is in survival mode
                    capabilitySyncManager.handleRecvFromClient(packet.capability,contextSupplier.get().getSender());
                }
                contextSupplier.get().setPacketHandled(true);
            }
        });

        INSTANCE.registerMessage(cid++,attackPacket.class, attackPacket.encoder, attackPacket.decoder, new BiConsumer<attackPacket, Supplier<NetworkEvent.Context>>() {
            @Override
            public void accept(attackPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
                var plr = contextSupplier.get().getSender();
                var entity = plr.getLevel().getEntity(packet.targetID);
                assert entity != null;
                attackPacket.applyKnockback(entity,packet.angle);
                contextSupplier.get().setPacketHandled(true);
            }
        });
    }*/
}
