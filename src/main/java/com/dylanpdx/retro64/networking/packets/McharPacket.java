package com.dylanpdx.retro64.networking.packets;

import com.dylanpdx.retro64.Retro64;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public record McharPacket(Vector3f pos,
                          byte[] animInfo,
                          Vector3f animRot,
                          int action,int model, GameProfile player) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<McharPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Retro64.MOD_ID, "mchardata"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf,McharPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,McharPacket::pos,
            ByteBufCodecs.BYTE_ARRAY,McharPacket::animInfo,
            ByteBufCodecs.VECTOR3F,McharPacket::animRot,
            ByteBufCodecs.INT,McharPacket::action,
            ByteBufCodecs.INT,McharPacket::model,
            ByteBufCodecs.GAME_PROFILE,McharPacket::player,
            McharPacket::new
    );

    public static McharPacket clone(GameProfile player,McharPacket packet){
        return new McharPacket(packet.pos(),packet.animInfo(),packet.animRot(),packet.action(),packet.model(),player);
    }
}