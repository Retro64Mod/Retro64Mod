package com.dylanpdx.retro64.networking.packets;

import com.dylanpdx.retro64.Retro64;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HealPacket(byte amount) implements CustomPacketPayload {
    public static final Type<HealPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID, "heal"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, HealPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, HealPacket::amount,
            HealPacket::new
    );
}