package com.dylanpdx.retro64.maps;

import com.dylanpdx.retro64.Utils;
import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Maps a material to a block property.
 */
public class BlockMatMaps {
    public static final TagKey<Block> isCubic = TagKey.create(BuiltInRegistries.BLOCK.key(), Utils.resourceLocationFromNamespaceAndPath("retro64", "collision/is_cubic"));
    public static final TagKey<Block> isFlat = TagKey.create(BuiltInRegistries.BLOCK.key(), Utils.resourceLocationFromNamespaceAndPath("retro64", "collision/is_flat"));
    public static final TagKey<Block> useModel = TagKey.create(BuiltInRegistries.BLOCK.key(), Utils.resourceLocationFromNamespaceAndPath("retro64", "collision/use_model"));
    public static final TagKey<Block> maxHitbox = TagKey.create(BuiltInRegistries.BLOCK.key(), Utils.resourceLocationFromNamespaceAndPath("retro64", "collision/max_hitbox"));
    public static final TagKey<Block> vanishable = TagKey.create(BuiltInRegistries.BLOCK.key(), Utils.resourceLocationFromNamespaceAndPath("retro64", "collision/vanishable"));
    public static final TagKey<Block> intangible = TagKey.create(BuiltInRegistries.BLOCK.key(), Utils.resourceLocationFromNamespaceAndPath("retro64", "collision/intangible"));

    /**
     * Get material property for a block depending on it's tag.
     *
     * @param block {@link BlockState} containing the block
     * @return {@link SM64SurfaceType} The surface type
     */
    public static SM64SurfaceType getFromTag(BlockState block) {

        boolean hard = block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.hard);

        if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.burning))
            return SM64SurfaceType.SURFACE_BURNING;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.hangable))
            return SM64SurfaceType.SURFACE_HANGABLE;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.slow))
            return SM64SurfaceType.SURFACE_SLOW;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.shallow_quicksand))
            return SM64SurfaceType.SURFACE_SHALLOW_QUICKSAND;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.quicksand))
            return SM64SurfaceType.SURFACE_QUICKSAND;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.deep_quicksand))
            return SM64SurfaceType.SURFACE_DEEP_QUICKSAND;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.death_quicksand))
            return SM64SurfaceType.SURFACE_INSTANT_QUICKSAND;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.not_slippery))
            return hard ? SM64SurfaceType.SURFACE_HARD_NOT_SLIPPERY : SM64SurfaceType.SURFACE_NOT_SLIPPERY;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.slippery))
            return hard ? SM64SurfaceType.SURFACE_HARD_SLIPPERY : SM64SurfaceType.SURFACE_SLIPPERY;
        else if (block.getTags().anyMatch(t -> t == SM64SurfaceType.Tags.very_slippery))
            return hard ? SM64SurfaceType.SURFACE_HARD_VERY_SLIPPERY : SM64SurfaceType.SURFACE_VERY_SLIPPERY;

        return SM64SurfaceType.SURFACE_DEFAULT;
    }

    /**
     * Get if the block's collision should be replaced with a full solid cube.
     *
     * @param block MC material name as registry name
     * @return true if the block's collision should be replaced with a full solid cube
     */
    public static boolean replaceCollisionMat(BlockState block) {
        return block.getTags().anyMatch(t -> t == isCubic);
    }

    /**
     * Get if the block's collision should be replaced with a flat plane.
     *
     * @param block MC material name as registry name
     * @return true if the block's collision should be replaced with a flat plane
     */
    public static boolean flatCollisionMat(BlockState block) {
        return block.getTags().anyMatch(t -> t == isFlat);
    }

}
