package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.Retro64;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.joml.Vector3f;

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

    public record McharPacket(Vector3f pos,
                              byte[] animInfo,
                              short animRotX, short animRotY, short animRotZ,
                              int action) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<McharPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID, "mchardata"));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static final StreamCodec<ByteBuf,McharPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VECTOR3F,McharPacket::pos,
                ByteBufCodecs.BYTE_ARRAY,McharPacket::animInfo,
                ByteBufCodecs.SHORT,McharPacket::animRotX,
                ByteBufCodecs.SHORT,McharPacket::animRotY,
                ByteBufCodecs.SHORT,McharPacket::animRotZ,
                ByteBufCodecs.INT,McharPacket::action,
                McharPacket::new
        );
    }
}
