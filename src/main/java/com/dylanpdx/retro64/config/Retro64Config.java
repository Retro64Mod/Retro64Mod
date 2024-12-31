package com.dylanpdx.retro64.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Retro64Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec CONFIG_SPEC;

    public static final ModConfigSpec.ConfigValue<String> ROM_PATH;

    static{
        BUILDER.push("Retro64 Config");
        ROM_PATH = BUILDER.comment("The path to the ROM file to be loaded").define("romPath", "mods/baserom.us.z64");
        BUILDER.pop();
        CONFIG_SPEC = BUILDER.build();
    }
}
