package com.dylanpdx.retro64.capabilities;

import com.dylanpdx.retro64.Utils;
import com.dylanpdx.retro64.networking.SM64PacketHandler;
import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.networking.capabilityPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;

public class capabilitySyncManager {

    static HashMap<Player,smc64CapabilityInterface> cache = new HashMap<>(); // Server caches who's in R64 mode to fwd to other clients

    // Send capability from local to server, which will be synced to all other clients
    public static void syncClientToServer(smc64CapabilityInterface capabiliity,boolean kill){
        SM64PacketHandler.INSTANCE.sendToServer(new capabilityPacket(capabiliity,kill));
    }

    public static void handleRecvFromClient(smc64CapabilityInterface capabiliity, Player p){
        cache.put(p,capabiliity);
        syncServerToClient(p);

        var smCap = Utils.getSmc64Capability(p);
        smCap.setIsEnabled(capabiliity.getIsEnabled());
    }

    // Send capability from server to all clients
    public static void syncServerToClient(Player p){
        var cap = cache.get(p);
        if (cap != null){
            SM64PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> p),new capabilityPacket((smc64CapabilityImplementation) cap,p));
        }
    }

    // Handle getting data from the server about a player's capabilities
    public static void handleRecvFromServer(smc64CapabilityInterface capabiliity, Player p){
        p.getCapability(smc64Capability.INSTANCE).ifPresent(cap -> {
            if (capabiliity.getIsEnabled())
                RemoteMCharHandler.mCharOn(p);
            else
                RemoteMCharHandler.mCharOff(p);
        });
    }

}
