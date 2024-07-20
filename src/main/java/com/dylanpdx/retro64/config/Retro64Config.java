package com.dylanpdx.retro64.config;

import net.neoforged.neoforge.common.ForgeConfigSpec;

public final class Retro64Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG_SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> ROM_PATH;

    static{
        BUILDER.push("Retro64 Config");
        ROM_PATH = BUILDER.comment("The path to the ROM file to be loaded").define("romPath", "mods/baserom.us.z64");
        BUILDER.pop();
        CONFIG_SPEC = BUILDER.build();
    }
}
