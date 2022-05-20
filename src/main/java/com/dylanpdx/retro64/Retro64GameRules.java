package com.dylanpdx.retro64;

import net.minecraft.world.level.GameRules;

public class Retro64GameRules {
    public static GameRules.Key<GameRules.BooleanValue> forceMario;

    public static void register(){
        forceMario = GameRules.register("forceMario", GameRules.Category.PLAYER,mappingsConvert.createBooleanValue(false));
    }
}
