package com.dylanpdx.retro64.sm64.libsm64;

import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;

public class PUFixer {

    /**
     * Takes Coordinate in MC and converts it to SM64 using the scale factor
     * @param pos Coordinate in MC
     * @return Coordinate in SM64
     */
    public static Vector3f convertToSM64(Vector3f pos){
        pos.mul(LibSM64.SCALE_FACTOR);

        return pos;
    }

    /**
     * Takes Coordinate in MC and converts it to SM64 using the scale factor
     * @param pos Coordinate in MC
     * @return Coordinate in SM64
     */
    public static Vec3 convertToSM64(Vec3 pos){
        return pos.scale(LibSM64.SCALE_FACTOR);
    }

    /**
     * Takes Coordinate in SM64 and converts it to MC using the scale factor
     * @param pos Coordinate in SM64
     * @return Coordinate in MC
     */
    public static Vector3f convertToMC(Vector3f pos)
    {
        pos.mul(1/LibSM64.SCALE_FACTOR);
        return pos;
    }
}
