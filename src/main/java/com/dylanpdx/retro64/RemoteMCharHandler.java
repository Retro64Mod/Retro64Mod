package com.dylanpdx.retro64;

import com.dylanpdx.retro64.capabilities.capabilitySyncManager;
import com.dylanpdx.retro64.sm64.libsm64.AnimInfo;
import com.dylanpdx.retro64.sm64.libsm64.MChar;
import com.dylanpdx.retro64.sm64.libsm64.SM64MCharState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Hashtable;

/**
 * Handles checking if a player has R64 enabled, getting it's MChar, etc.
 */
public class RemoteMCharHandler {

    static Hashtable<Player, MChar> mChars = new Hashtable<>();
    public static ResourceKey<Level> wasMCharDimm=null;

    static void verifyInitialized(){
        if (mChars==null){
            mChars = new Hashtable<>();
        }
    }

    /**
     * Update animation for all players
     */
    public static void tickAll(){
        verifyInitialized();
        for(Player player : mChars.keySet()){
            mChars.get(player).animUpdate();
        }
    }

    /**
     * Update a single mChar's animation
     * @param player Player to update
     * @param animInfo Animation info to update
     * @param animXRot Animation X rotation
     * @param animYRot Animation Y rotation
     * @param animZRot Animation Z rotation
     * @param action MChar action
     * @param model Model the player is using
     * @param pos Player position
     */
    public static void updateMChar(Player player, AnimInfo animInfo, short animXRot, short animYRot, short animZRot, int action, int model, Vec3 pos){
        verifyInitialized();
        if (!mChars.containsKey(player)){
            SM64EnvManager.updateSurfs(null, SM64EnvManager.generateSafetyFloor(0,0,0));
            mChars.put(player, new MChar());
        }
        mChars.get(player).animInfo =animInfo;
        mChars.get(player).teleport(player.position());
        mChars.get(player).animXRot = animXRot;
        mChars.get(player).animYRot = animYRot;
        mChars.get(player).animZRot = animZRot;
        mChars.get(player).state.action = action;
        mChars.get(player).state.currentModel = model;
        mChars.get(player).state.position[0] = (float)pos.x;
        mChars.get(player).state.position[1] = (float)pos.y;
        mChars.get(player).state.position[2] = (float)pos.z;
    }

    /**
     * Turn on R64 mode for a player
     * @param player Player to turn on
     */
    public static void mCharOn(Player player){
        if (player.isSpectator()) // don't turn on R64 for spectators
            return;
        var smCap = Utils.getSmc64Capability(player);
        if (smCap==null) return;
        boolean isMChar =smCap.getIsEnabled();
        if (isMChar) return;
        smCap.setIsEnabled(true);
        if (player== Minecraft.getInstance().player)
        {
            // handle for local player
            if (SM64EnvManager.selfMChar==null)
                SM64EnvManager.selfMChar = new MChar();
            capabilitySyncManager.syncClientToServer(smCap,false);
            wasMCharDimm=player.getLevel().dimension();
        }
        else
        {
            // create a new mChar
            SM64EnvManager.updateSurfs(null, SM64EnvManager.generateSafetyFloor(0,0,0));
            mChars.put(player, new MChar());
        }
        onMCharToggle(player,true);
    }

    /**
     * Turn off R64 mode for a player
     * @param player Player to turn off
     */
    public static void mCharOff(Player player){
        mCharOff(player,false);
    }

    /**
     * Turn off R64 mode for a player
     * @param player Player to turn off
     * @param fatal If true, the player will die; used for when MChar health is 0
     */
    public static void mCharOff(Player player,boolean fatal){
        var smCap = Utils.getSmc64Capability(player);
        if (smCap==null) return;
        boolean isMChar =smCap.getIsEnabled();
        if (!isMChar) return;
        smCap.setIsEnabled(false);
        player.moveTo(player.position().x,Math.round(player.position().y),player.position().z);
        if (player== Minecraft.getInstance().player)
        {
            SM64EnvManager.selfMChar.destroy();
            SM64EnvManager.selfMChar = null;
            wasMCharDimm=null;
            capabilitySyncManager.syncClientToServer(smCap,fatal);
        }
        else
        {
            mChars.remove(player).destroy();
        }
        onMCharToggle(player,false);
    }

    /**
     * Toggle R64 mode for a player
     * @param player Player to toggle
     */
    public static void toggleMChar(Player player){
        var smCap = Utils.getSmc64Capability(player);
        boolean isMChar = smCap != null && smCap.getIsEnabled();
        if (isMChar) mCharOff(player);
        else mCharOn(player);
    }

    /**
     * Is the player in R64 mode?
     * @param player Player to check
     * @return True if the player is in R64 mode
     */
    public static boolean getIsMChar(Player player){
        var smCap = Utils.getSmc64Capability(player);
        if (smCap==null) return false;
        return smCap.getIsEnabled();
    }

    /**
     * Set MChar status for a player
     * @param player Player to set
     * @param isMChar True if the player should be in R64 mode
     */
    public static void setIsMChar(Player player, boolean isMChar){
        if (isMChar){
            mCharOn(player);
        }else{
            mCharOff(player);
        }
    }

    /**
     * Called when mChar mode is toggled
     * @param player Player to toggle
     * @param isMChar True if the player is in R64 mode
     */
    public static void onMCharToggle(Player player,boolean isMChar){
        player.refreshDimensions();
        var pos = player.position();
        for (int i = 0; i < 50; i++) {
            var rx = (Minecraft.getInstance().level.random.nextFloat()-.5f)/3f;
            var ry = (Minecraft.getInstance().level.random.nextFloat()-0)/3f;
            var rz = (Minecraft.getInstance().level.random.nextFloat()-.5f)/3f;
            Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.FIREWORK,pos.x,pos.y,pos.z,rx,ry,rz);
        }
    }

    /**
     * Get the MChar's state for a player
     * @param p Player to get the state for
     * @return The MChar's state
     */
    public static SM64MCharState getState(Player p){
        verifyInitialized();
        if (mChars.containsKey(p)){
            return mChars.get(p).state;
        }else if (p==Minecraft.getInstance().player){
            return SM64EnvManager.selfMChar.state;
        }
        return null;
    }

    /**
     * returns all players who are mChar
     * @return all players who are mChar
     */
    public static Player[] getPlayers(){
        verifyInitialized();
        return mChars.keySet().toArray(new Player[0]);
    }

}
