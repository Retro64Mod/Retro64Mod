package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.networking.packets.McharPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Retro64Net {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(McharPacket.TYPE, McharPacket.STREAM_CODEC,
        new DirectionalPayloadHandler<>(
                ClientPayloadHandler::handleMcharPacket,
                ServerPayloadHandler::handleMcharPacket)
        );
    }
}
