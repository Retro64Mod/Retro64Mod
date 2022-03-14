package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.sm64.libsm64.AnimInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * model packet, syncs information about the player's model/animation from server -> client
 *
 */
public class modelPacket {

    public AnimInfo animInfo;
    public short animXRot,animYRot,animZRot;
    public int action,model;
    public Vec3 pos;
    public UUID user;

    public static final BiConsumer<modelPacket, FriendlyByteBuf> encoder = new BiConsumer<modelPacket, FriendlyByteBuf>() {
        @Override
        public void accept(modelPacket modelPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeUUID(modelPacket.user);
            try {
                friendlyByteBuf.writeByteArray(modelPacket.animInfo.serialize());
            } catch (IOException e) {
                e.printStackTrace();
            }
            friendlyByteBuf.writeShort(modelPacket.animXRot);
            friendlyByteBuf.writeShort(modelPacket.animYRot);
            friendlyByteBuf.writeShort(modelPacket.animZRot);
            friendlyByteBuf.writeInt(modelPacket.action);
            friendlyByteBuf.writeInt(modelPacket.model);
            friendlyByteBuf.writeDouble(modelPacket.pos.x);
            friendlyByteBuf.writeDouble(modelPacket.pos.y);
            friendlyByteBuf.writeDouble(modelPacket.pos.z);
        }
    };


    public static Function<FriendlyByteBuf, modelPacket> decoder = new Function<FriendlyByteBuf, modelPacket>() {
        @Override
        public modelPacket apply(FriendlyByteBuf friendlyByteBuf) {
            modelPacket modelPacket = new modelPacket();
            modelPacket.user = friendlyByteBuf.readUUID();
            try {
                modelPacket.animInfo = AnimInfo.deserialize(friendlyByteBuf.readByteArray());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            modelPacket.animXRot = friendlyByteBuf.readShort();
            modelPacket.animYRot = friendlyByteBuf.readShort();
            modelPacket.animZRot = friendlyByteBuf.readShort();
            modelPacket.action = friendlyByteBuf.readInt();
            modelPacket.model = friendlyByteBuf.readInt();
            modelPacket.pos = new Vec3(friendlyByteBuf.readDouble(),friendlyByteBuf.readDouble(),friendlyByteBuf.readDouble());

            return modelPacket;
        }
    };

    public modelPacket(){
        animInfo=null;
    }

    public modelPacket(AnimInfo animInfo,short animXRot,short animYRot,short animZRot,int action,int model,Vec3 pos, UUID user){
        this.user = user;
        this.animInfo = animInfo;
        this.animXRot = animXRot;
        this.animYRot = animYRot;
        this.animZRot = animZRot;
        this.action = action;
        this.model = model;
        this.pos=pos;
    }

}
