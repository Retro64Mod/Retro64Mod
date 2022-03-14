package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.sm64.libsm64.AnimInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * MChar Packet - Syncs player position, animation, and model from client to server
 * The server -> client version is in modelPacket
 */
public class mCharPacket {

    Vec3 pos;
    AnimInfo animInfo;
    public short animXRot,animYRot,animZRot;
    public int action,model;
    public Player player;

    public static final BiConsumer<mCharPacket, FriendlyByteBuf> encoder = new BiConsumer<mCharPacket, FriendlyByteBuf>(){
        @Override
        public void accept(mCharPacket mCharPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeDouble(mCharPacket.pos.x());
            friendlyByteBuf.writeDouble(mCharPacket.pos.y());
            friendlyByteBuf.writeDouble(mCharPacket.pos.z());
            try {
                friendlyByteBuf.writeByteArray(mCharPacket.animInfo.serialize());
            } catch (IOException e) {
                e.printStackTrace();
            }
            friendlyByteBuf.writeShort(mCharPacket.animXRot);
            friendlyByteBuf.writeShort(mCharPacket.animYRot);
            friendlyByteBuf.writeShort(mCharPacket.animZRot);
            friendlyByteBuf.writeInt(mCharPacket.action);
            friendlyByteBuf.writeInt(mCharPacket.model);
        }
    };

    public static final Function<FriendlyByteBuf, mCharPacket> decoder = new Function<FriendlyByteBuf, mCharPacket>(){

        @Override
        public mCharPacket apply(FriendlyByteBuf friendlyByteBuf) {
            mCharPacket mp = new mCharPacket();
            mp.pos = new Vec3(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
            byte[] bytes = friendlyByteBuf.readByteArray();
            try {
                mp.animInfo = AnimInfo.deserialize(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.animXRot = friendlyByteBuf.readShort();
            mp.animYRot = friendlyByteBuf.readShort();
            mp.animZRot = friendlyByteBuf.readShort();
            mp.action = friendlyByteBuf.readInt();
            mp.model = friendlyByteBuf.readInt();
            return mp;
        }
    };

    public mCharPacket() {
        this.pos = new Vec3(0, 0, 0);
        this.animInfo = null;

    }

    public mCharPacket(Vec3 pos, AnimInfo animInfo,short animXRot,short animYRot,short animZRot,int action,int model,Player player) {
        this.pos = pos;
        this.animInfo = animInfo;
        this.animXRot = animXRot;
        this.animYRot = animYRot;
        this.animZRot = animZRot;
        this.player = player;
        this.action = action;
        this.model = model;
    }

}