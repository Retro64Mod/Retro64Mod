package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.Utils;
import com.dylanpdx.retro64.networking.packets.McharPacket;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleMcharPacket(final McharPacket data, final IPayloadContext context) {
        // ensure the client can't change the player's game profile
        PacketDistributor.sendToPlayersTrackingEntity(context.player(),McharPacket.clone(context.player().getGameProfile(),data));
        if (data.model() != -1 && data.action() != -1)
        {
            context.player().setPos(data.pos().x(),data.pos().y(),data.pos().z());
            Utils.setIsMarioData(context.player(),true);
        }else{
            Utils.setIsMarioData(context.player(),false);
        }
    }
}
