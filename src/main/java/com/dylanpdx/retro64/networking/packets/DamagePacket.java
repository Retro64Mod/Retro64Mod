package com.dylanpdx.retro64.networking.packets;

import com.dylanpdx.retro64.Retro64;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public record DamagePacket(Vector3f pos, byte damage) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamagePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID, "amount"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, DamagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, DamagePacket::pos,
            ByteBufCodecs.BYTE, DamagePacket::damage,
            DamagePacket::new
    );
}