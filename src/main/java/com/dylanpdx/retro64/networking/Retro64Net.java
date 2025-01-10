package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.networking.packets.DamagePacket;
import com.dylanpdx.retro64.networking.packets.HealPacket;
import com.dylanpdx.retro64.networking.packets.McharPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Retro64Net {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("2");
        registrar.playBidirectional(McharPacket.TYPE, McharPacket.STREAM_CODEC,
        new DirectionalPayloadHandler<>(
                ClientPayloadHandler::handleMcharPacket,
                ServerPayloadHandler::handleMcharPacket)
        );

        registrar.playToClient(DamagePacket.TYPE, DamagePacket.STREAM_CODEC,
                ClientPayloadHandler::handleDamagePacket
        );

        registrar.playToClient(HealPacket.TYPE, HealPacket.STREAM_CODEC,
                ClientPayloadHandler::handleHealPacket
        );
    }
}
