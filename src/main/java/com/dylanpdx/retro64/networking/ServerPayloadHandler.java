package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.networking.packets.McharPacket;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleMcharPacket(final McharPacket data, final IPayloadContext context) {
        PacketDistributor.sendToPlayersTrackingEntity(context.player(),data);
    }
}
