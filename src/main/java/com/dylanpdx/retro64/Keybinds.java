package com.dylanpdx.retro64;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class Keybinds {
    private static KeyMapping[] keyBindings;

    public static void register(){
        // 263-262
        keyBindings = new KeyMapping[]{
                new KeyMapping("key."+Retro64.MOD_ID+".actKey", InputConstants.KEY_LALT/*Left Alt*/, "key.categories.retro64"),
                new KeyMapping("key."+Retro64.MOD_ID+".mToggle", InputConstants.KEY_M/*M*/, "key.categories.retro64"),
                new KeyMapping("key."+Retro64.MOD_ID+".mMenu", InputConstants.KEY_Z/*Z*/, "key.categories.retro64"),
                //new KeyMapping("key."+Retro64.MOD_ID+".debugToggle",InputConstants.KEY_RBRACKET/*Right Bracket*/, "key.categories.retro64"),
        };
        for (KeyMapping key : keyBindings) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    public static KeyMapping getActKey(){
        return keyBindings[0];
    }

    public static KeyMapping getMToggle(){
        return keyBindings[1];
    }

    public static KeyMapping getMMenu(){
        return keyBindings[2];
    }

    /*public static KeyMapping getDebugToggle(){
        return keyBindings[3];
    }*/

}
