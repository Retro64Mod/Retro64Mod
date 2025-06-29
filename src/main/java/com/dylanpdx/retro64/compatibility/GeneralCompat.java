package com.dylanpdx.retro64.compatibility;

import com.dylanpdx.retro64.compatibility.create.CreateCompat;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.fml.ModList;

public class GeneralCompat {
    private static byte createStatus=0;
    public static boolean hasCreate() {
        return false; // Currently disabled due to bugs; TODO: Fix Invalid memory access on contraption destroy
        /*if (createStatus==0)
            createStatus = ModList.get().isLoaded("create") ? (byte)2 : (byte)1;
        return createStatus==2;*/
    }

    public static void handleEntityLeave(EntityLeaveLevelEvent event){
        if (hasCreate()){
            CreateCompat.handleEntityLeave(event);
        }
    }

    public static void onUpdateWorldGeometry(LocalPlayer plr){
        if (hasCreate()){
            CreateCompat.onupdateWorldGeometry(plr);
        }
    }
}
