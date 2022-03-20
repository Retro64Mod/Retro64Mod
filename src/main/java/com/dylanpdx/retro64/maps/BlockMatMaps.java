package com.dylanpdx.retro64.maps;

import com.dylanpdx.retro64.sm64.SM64SurfaceType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Maps a material to a block property.
 */
public class BlockMatMaps {

    static final HashMap<String, Byte> SOLID_MATS; // Blocks with surface type overrides
    static final HashMap<String, Byte> NONSOLID_MATS; // non-solid blocks with surface type overrides
    static final ArrayList<String> REPLACE_COLLISION_MATS; // Blocks where their collision will be replaced with a cube
    static final ArrayList<String> FLAT_COLLISION_MATS; // Blocks where their collision will be replaced with a flat plane
    static {
        SOLID_MATS = new HashMap<String, Byte>();
        SOLID_MATS.put("minecraft:magma_block", SM64SurfaceType.Burning);
        SOLID_MATS.put("minecraft:iron_bars", SM64SurfaceType.Hangable);
        SOLID_MATS.put("minecraft:ice", SM64SurfaceType.Slippery);
        SOLID_MATS.put("minecraft:packed_ice", SM64SurfaceType.VerySlippery);
        SOLID_MATS.put("minecraft:blue_ice", SM64SurfaceType.VerySlippery);

        NONSOLID_MATS=new HashMap<>();
        NONSOLID_MATS.put("minecraft:lava", SM64SurfaceType.Burning);
        NONSOLID_MATS.put("minecraft:fire", SM64SurfaceType.Burning);

        REPLACE_COLLISION_MATS=new ArrayList<>();
        REPLACE_COLLISION_MATS.add("minecraft:iron_bars");
        REPLACE_COLLISION_MATS.add("minecraft:lava");

        FLAT_COLLISION_MATS=new ArrayList<>();
        FLAT_COLLISION_MATS.add("minecraft:fire");
    }

    /**
     * Get material property for a block that is solid.
     * @param mat MC material name as registry name
     * @return SM64 surface type
     */
    public static byte getSolidMat(String mat) {
        if (!SOLID_MATS.containsKey(mat)) {
            return SM64SurfaceType.Default;
        }
        return SOLID_MATS.get(mat);
    }

    /**
     * Get material property for a block that is not solid (i.e lava).
     * @param mat MC material name as registry name
     * @return SM64 surface type
     */
    public static byte getNonsolidMat(String mat) {
        if (!NONSOLID_MATS.containsKey(mat)) {
            return SM64SurfaceType.Default;
        }
        return NONSOLID_MATS.get(mat);
    }

    /**
     * Get if the block's collision should be replaced with a full solid cube.
     * @param mat MC material name as registry name
     * @return true if the block's collision should be replaced with a full solid cube
     */
    public static boolean replaceCollisionMat(String mat) {
        return REPLACE_COLLISION_MATS.contains(mat);
    }

    /**
     * Get if the block's collision should be replaced with a flat plane.
     * @param mat MC material name as registry name
     * @return true if the block's collision should be replaced with a flat plane
     */
    public static boolean flatCollisionMat(String mat) {
        return FLAT_COLLISION_MATS.contains(mat);
    }

}
