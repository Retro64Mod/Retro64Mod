package com.dylanpdx.retro64;

import com.dylanpdx.retro64.blocks.CastleStairsBlock;
import com.dylanpdx.retro64.blocks.DeepQuicksandBlock;
import com.dylanpdx.retro64.blocks.InstantQuicksandBlock;
import com.dylanpdx.retro64.items.MetalCap;
import com.dylanpdx.retro64.items.VanishCap;
import com.dylanpdx.retro64.items.WingCap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegistryHandler {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Retro64.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Retro64.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Retro64.MOD_ID);

    public static void init(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }

    public static final DeferredHolder<Block,Block> CASTLE_STAIRS = BLOCKS.register("castlestairs", CastleStairsBlock::new);
    public static final DeferredHolder<Block,Block> DEEP_QUICKSAND = BLOCKS.register("deep_quicksand", DeepQuicksandBlock::new);
    public static final DeferredHolder<Block,Block> INSTANT_QUICKSAND = BLOCKS.register("instant_quicksand", InstantQuicksandBlock::new);
    public static final DeferredHolder<Item,Item> CASTLE_STAIRS_ITEM = ITEMS.register("castlestairs",()->new BlockItem(CASTLE_STAIRS.get(),new Item.Properties()));
    public static final DeferredHolder<Item,Item> DEEP_QUICKSAND_ITEM = ITEMS.register("deep_quicksand",()->new BlockItem(DEEP_QUICKSAND.get(),new Item.Properties()));
    public static final DeferredHolder<Item,Item> INSTANT_QUICKSAND_ITEM = ITEMS.register("instant_quicksand",()->new BlockItem(INSTANT_QUICKSAND.get(),new Item.Properties()));
    public static final DeferredHolder<Item,Item> WING_CAP_ITEM = ITEMS.register("wing_cap",()->new WingCap(new Item.Properties()));
    public static final DeferredHolder<Item,Item> VANISH_CAP_ITEM = ITEMS.register("vanish_cap",()->new VanishCap(new Item.Properties()));
    public static final DeferredHolder<Item,Item> METAL_CAP_ITEM = ITEMS.register("metal_cap",()->new MetalCap(new Item.Properties()));

    public static final Supplier<CreativeModeTab> RETRO64_TAB = CREATIVE_TABS.register("retro64",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup."+Retro64.MOD_ID+"Tab"))
            .icon(() -> new ItemStack(WING_CAP_ITEM.get()))
            .displayItems((params,output)->{
                output.accept(RegistryHandler.CASTLE_STAIRS_ITEM.get());
                output.accept(RegistryHandler.DEEP_QUICKSAND_ITEM.get());
                output.accept(RegistryHandler.INSTANT_QUICKSAND_ITEM.get());
                output.accept(RegistryHandler.WING_CAP_ITEM.get());
                output.accept(RegistryHandler.METAL_CAP_ITEM.get());
                output.accept(RegistryHandler.VANISH_CAP_ITEM.get());
            })
            .build()
    );
}
