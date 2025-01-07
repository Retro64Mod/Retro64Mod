package com.dylanpdx.retro64.compatibility;

//import com.dylanpdx.retro64.compatibility.create.CreateCompat;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;

public class GeneralCompat {
    public static boolean hasCreate() {
        return false;
    }

    public static void handleEntityLeave(EntityLeaveLevelEvent event){
        if (hasCreate()){
            //CreateCompat.handleEntityLeave(event);
        }
    }

    public static void onUpdateWorldGeometry(LocalPlayer plr){
        if (hasCreate()){
            //CreateCompat.onupdateWorldGeometry(plr);
        }
    }
}
