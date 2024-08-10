package com.dylanpdx.retro64.networking;


import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Capability packet - Syncs capability data between client and server
 */
public class capabilityPacket {

    /*smc64CapabilityInterface capability;
    UUID pUUID;
    boolean kill;


    public Player getPlayer(){
        return Minecraft.getInstance().level.getPlayerByUUID(pUUID);
    }

    public void setPlayer(Player player){
        this.pUUID=player.getUUID();
    }

    public static final BiConsumer<capabilityPacket, FriendlyByteBuf> encoder = new BiConsumer<capabilityPacket, FriendlyByteBuf>() {
        @Override
        public void accept(capabilityPacket capabilityPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeNbt(capabilityPacket.capability.serializeNBT());
            friendlyByteBuf.writeBoolean(capabilityPacket.pUUID!=null);
            if(capabilityPacket.pUUID!=null){
                friendlyByteBuf.writeUUID(capabilityPacket.pUUID);
            }
            friendlyByteBuf.writeBoolean(capabilityPacket.kill);
        }
    };

    public static Function<FriendlyByteBuf, capabilityPacket> decoder = new Function<FriendlyByteBuf, capabilityPacket>() {
        @Override
        public capabilityPacket apply(FriendlyByteBuf friendlyByteBuf) {
            var cpacket = new capabilityPacket();
            cpacket.capability = new smc64CapabilityImplementation();
            cpacket.capability.deserializeNBT(friendlyByteBuf.readNbt());
            boolean hasPlayer = friendlyByteBuf.readBoolean();
            if(hasPlayer){
                cpacket.pUUID = friendlyByteBuf.readUUID();
            }
            cpacket.kill = friendlyByteBuf.readBoolean();
            return cpacket;
        }
    };

    public capabilityPacket(smc64CapabilityInterface capability, Player p){
        this.capability=capability;
        setPlayer(p);
    }

    public capabilityPacket(smc64CapabilityInterface capability,UUID pUUID){
        this.capability=capability;
        this.pUUID=pUUID;
    }

    public capabilityPacket(smc64CapabilityInterface capability,boolean kill){
        this.capability=capability;
        this.kill=kill;
    }

    capabilityPacket(){

    }*/
}
