package com.dylanpdx.retro64.networking;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Heal packet - Syncs when a player should heal
 */
public class healPacket {

    byte healAmount;

    public static final BiConsumer<healPacket, FriendlyByteBuf> encoder = new BiConsumer<healPacket, FriendlyByteBuf>() {
        @Override
        public void accept(healPacket dmgPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeByte(dmgPacket.healAmount);
        }
    };


    public static Function<FriendlyByteBuf, healPacket> decoder = new Function<FriendlyByteBuf, healPacket>() {
        @Override
        public healPacket apply(FriendlyByteBuf friendlyByteBuf) {
            healPacket dmgPacket = new healPacket();
            dmgPacket.healAmount = friendlyByteBuf.readByte();
            return dmgPacket;
        }
    };

    private healPacket(){
        healAmount = 0;
    }

    public healPacket(byte healAmount){
        this.healAmount = healAmount;
    }

}
