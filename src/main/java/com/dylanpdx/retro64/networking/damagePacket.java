package com.dylanpdx.retro64.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Damage packet - syncs damage to clients
 */
public class damagePacket {

    Vec3 pos;
    int damageAmount;

    public static final BiConsumer<damagePacket, FriendlyByteBuf> encoder = new BiConsumer<damagePacket, FriendlyByteBuf>() {
        @Override
        public void accept(damagePacket dmgPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeDouble(dmgPacket.pos.x);
            friendlyByteBuf.writeDouble(dmgPacket.pos.y);
            friendlyByteBuf.writeDouble(dmgPacket.pos.z);
            friendlyByteBuf.writeInt(dmgPacket.damageAmount);
        }
    };


    public static Function<FriendlyByteBuf, damagePacket> decoder = new Function<FriendlyByteBuf, damagePacket>() {
        @Override
        public damagePacket apply(FriendlyByteBuf friendlyByteBuf) {
            damagePacket dmgPacket = new damagePacket();
            dmgPacket.pos = new Vec3(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble());
            dmgPacket.damageAmount = friendlyByteBuf.readInt();
            return dmgPacket;
        }
    };

    public damagePacket(){
        pos = new Vec3(0,0,0);
        damageAmount = 0;
    }

    public damagePacket(Vec3 pos, int damageAmount){
        this.pos = pos;
        this.damageAmount = damageAmount;
    }

}
