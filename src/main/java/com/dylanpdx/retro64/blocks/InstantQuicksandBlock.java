package com.dylanpdx.retro64.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class InstantQuicksandBlock extends Block {

    public InstantQuicksandBlock() {
        super(BlockBehaviour.Properties.of(Material.SAND)
                .strength(3.5f,4.8f)
                .sound(SoundType.SAND).noOcclusion());
        this.registerDefaultState(this.defaultBlockState());
    }
}
