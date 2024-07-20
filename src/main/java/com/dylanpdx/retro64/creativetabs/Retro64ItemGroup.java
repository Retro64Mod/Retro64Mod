package com.dylanpdx.retro64.creativetabs;

import com.dylanpdx.retro64.RegistryHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class Retro64ItemGroup {
    public static final CreativeModeTab RETRO64_TAB = new CreativeModeTab("retro64Tab") {
        @Override
        public ItemStack getIconItem() {
            return new ItemStack(RegistryHandler.CASTLE_STAIRS_ITEM.get());
        }
    };
}