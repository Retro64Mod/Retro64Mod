package com.dylanpdx.retro64.networking;

import com.dylanpdx.retro64.RemoteMCharHandler;
import com.dylanpdx.retro64.Utils;
import com.mojang.math.Quaternion;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Attack packet, used to apply knockback to entities
 */
public class attackPacket {

    int targetID;
    float angle;

    public static final BiConsumer<attackPacket, FriendlyByteBuf> encoder = new BiConsumer<attackPacket, FriendlyByteBuf>(){

        @Override
        public void accept(attackPacket attackPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeInt(attackPacket.targetID);
            friendlyByteBuf.writeFloat(attackPacket.angle);
        }
    };

    public static final Function<FriendlyByteBuf,attackPacket> decoder = new Function<FriendlyByteBuf,attackPacket>(){

        @Override
        public attackPacket apply(FriendlyByteBuf friendlyByteBuf) {
            attackPacket attackPacket = new attackPacket(friendlyByteBuf.readInt(), friendlyByteBuf.readFloat());
            return attackPacket;
        }
    };

    public attackPacket(int eID, float ang){
        this.targetID = eID;
        this.angle = ang;
    }

    public static void applyKnockback(Entity e, float ang){
        // convert angle, which is in radians to vec3
        ang= (float) (Math.toRadians(90)-ang);
        Vec3 knockback = new Vec3(Math.cos(ang), .7f, Math.sin(ang));
        e.setDeltaMovement(e.getDeltaMovement().add(new Vec3(knockback.x(),knockback.y(),knockback.z())));

    }
}
