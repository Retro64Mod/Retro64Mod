package com.dylanpdx.retro64;

import com.dylanpdx.retro64.blocks.CastleStairsBlock;
import com.dylanpdx.retro64.blocks.DeepQuicksandBlock;
import com.dylanpdx.retro64.blocks.InstantQuicksandBlock;
import com.dylanpdx.retro64.creativetabs.Retro64ItemGroup;
import com.dylanpdx.retro64.items.MetalCap;
import com.dylanpdx.retro64.items.VanishCap;
import com.dylanpdx.retro64.items.WingCap;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryHandler {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Retro64.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Retro64.MOD_ID);


    public static void init(){
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> CASTLE_STAIRS = BLOCKS.register("castlestairs", CastleStairsBlock::new);
    public static final RegistryObject<Block> DEEP_QUICKSAND = BLOCKS.register("deep_quicksand", DeepQuicksandBlock::new);
    public static final RegistryObject<Block> INSTANT_QUICKSAND = BLOCKS.register("instant_quicksand", InstantQuicksandBlock::new);
    public static final RegistryObject<Item> CASTLE_STAIRS_ITEM = ITEMS.register("castlestairs",()->new BlockItem(CASTLE_STAIRS.get(),new Item.Properties().tab(Retro64ItemGroup.RETRO64_TAB)));
    public static final RegistryObject<Item> DEEP_QUICKSAND_ITEM = ITEMS.register("deep_quicksand",()->new BlockItem(DEEP_QUICKSAND.get(),new Item.Properties().tab(Retro64ItemGroup.RETRO64_TAB)));
    public static final RegistryObject<Item> INSTANT_QUICKSAND_ITEM = ITEMS.register("instant_quicksand",()->new BlockItem(INSTANT_QUICKSAND.get(),new Item.Properties().tab(Retro64ItemGroup.RETRO64_TAB)));
    public static final RegistryObject<Item> WING_CAP_ITEM = ITEMS.register("wing_cap",()->new WingCap(new Item.Properties().tab(Retro64ItemGroup.RETRO64_TAB)));
    public static final RegistryObject<Item> VANISH_CAP_ITEM = ITEMS.register("vanish_cap",()->new VanishCap(new Item.Properties().tab(Retro64ItemGroup.RETRO64_TAB)));
    public static final RegistryObject<Item> METAL_CAP_ITEM = ITEMS.register("metal_cap",()->new MetalCap(new Item.Properties().tab(Retro64ItemGroup.RETRO64_TAB)));

}
