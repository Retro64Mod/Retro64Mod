package com.dylanpdx.retro64.sm64;

public enum SM64SurfaceType {
     SURFACE_DEFAULT(0x0000), // Environment default
     SURFACE_BURNING(0x0001), // Lava / Frostbite (in SL), but is used mostly for Lava
     SURFACE_0004(0x0004), // Unused, has no function and has parameters
     SURFACE_HANGABLE(0x0005), // Ceiling that Mario can climb on
     SURFACE_SLOW(0x0009), // Slow down Mario, unused
     SURFACE_DEATH_PLANE(0x000A), // Death floor
     SURFACE_CLOSE_CAMERA(0x000B), // Close camera
     SURFACE_WATER(0x000D), // Water, has no action, used on some waterboxes below
     SURFACE_FLOWING_WATER(0x000E), // Water (flowing), has parameters
     SURFACE_INTANGIBLE(0x0012), // Intangible (Separates BBH mansion from merry-go-round, for room usage)
     SURFACE_VERY_SLIPPERY(0x0013), // Very slippery, mostly used for slides
     SURFACE_SLIPPERY(0x0014), // Slippery
     SURFACE_NOT_SLIPPERY(0x0015), // Non-slippery, climbable
     SURFACE_TTM_VINES(0x0016), // TTM vines, has no action defined
     SURFACE_MGR_MUSIC(0x001A), // Plays the Merry go round music, see handle_merry_go_round_music in bbh_merry_go_round.inc.c for more details
     SURFACE_INSTANT_WARP_1B(0x001B), // Instant warp to another area, used to warp between areas in WDW and the endless stairs to warp back
     SURFACE_INSTANT_WARP_1C(0x001C), // Instant warp to another area, used to warp between areas in WDW
     SURFACE_INSTANT_WARP_1D(0x001D), // Instant warp to another area, used to warp between areas in DDD, SSL and TTM
     SURFACE_INSTANT_WARP_1E(0x001E), // Instant warp to another area, used to warp between areas in DDD, SSL and TTM
     SURFACE_SHALLOW_QUICKSAND(0x0021), // Shallow Quicksand (depth of 10 units)
     SURFACE_DEEP_QUICKSAND(0x0022), // Quicksand (lethal, slow, depth of 160 units)
     SURFACE_INSTANT_QUICKSAND(0x0023), // Quicksand (lethal, instant)
     SURFACE_DEEP_MOVING_QUICKSAND(0x0024), // Moving quicksand (flowing, depth of 160 units)
     SURFACE_SHALLOW_MOVING_QUICKSAND(0x0025), // Moving quicksand (flowing, depth of 25 units)
     SURFACE_QUICKSAND(0x0026), // Moving quicksand (60 units)
     SURFACE_MOVING_QUICKSAND(0x0027), // Moving quicksand (flowing, depth of 60 units)
     SURFACE_WALL_MISC(0x0028), // Used for some walls, Cannon to adjust the camera, and some objects like Warp Pipe
     SURFACE_NOISE_DEFAULT(0x0029), // Default floor with noise
     SURFACE_NOISE_SLIPPERY(0x002A), // Slippery floor with noise
     SURFACE_HORIZONTAL_WIND(0x002C), // Horizontal wind, has parameters
     SURFACE_INSTANT_MOVING_QUICKSAND(0x002D), // Quicksand (lethal, flowing)
     SURFACE_ICE(0x002E), // Slippery Ice, in snow levels and THI's water floor
     SURFACE_LOOK_UP_WARP(0x002F), // Look up and warp (Wing cap entrance)
     SURFACE_HARD(0x0030), // Hard floor (Always has fall damage)
     SURFACE_WARP(0x0032), // Surface warp
     SURFACE_TIMER_START(0x0033), // Timer start (Peach's secret slide)
     SURFACE_TIMER_END(0x0034), // Timer stop (Peach's secret slide)
     SURFACE_HARD_SLIPPERY(0x0035), // Hard and slippery (Always has fall damage)
     SURFACE_HARD_VERY_SLIPPERY(0x0036), // Hard and very slippery (Always has fall damage)
     SURFACE_HARD_NOT_SLIPPERY(0x0037), // Hard and Non-slippery (Always has fall damage)
     SURFACE_VERTICAL_WIND(0x0038), // Death at bottom with vertical wind
     SURFACE_BOSS_FIGHT_CAMERA(0x0065), // Wide camera for BOB and WF bosses
     SURFACE_CAMERA_FREE_ROAM(0x0066), // Free roam camera for THI and TTC
     SURFACE_THI3_WALLKICK(0x0068), // Surface where there's a wall kick section in THI 3rd area, has no action defined
     SURFACE_CAMERA_8_DIR(0x0069), // Surface that enables far camera for platforms, used in THI
     SURFACE_CAMERA_MIDDLE(0x006E), // Surface camera that returns to the middle, used on the 4 pillars of SSL
     SURFACE_CAMERA_ROTATE_RIGHT(0x006F), // Surface camera that rotates to the right (Bowser 1 & THI)
     SURFACE_CAMERA_ROTATE_LEFT(0x0070), // Surface camera that rotates to the left (BOB & TTM)
     SURFACE_CAMERA_BOUNDARY(0x0072), // Intangible Area, only used to restrict camera movement
     SURFACE_NOISE_VERY_SLIPPERY_73(0x0073), // Very slippery floor with noise, unused
     SURFACE_NOISE_VERY_SLIPPERY_74(0x0074), // Very slippery floor with noise, unused
     SURFACE_NOISE_VERY_SLIPPERY(0x0075), // Very slippery floor with noise, used in CCM
     SURFACE_NO_CAM_COLLISION(0x0076), // Surface with no cam collision flag
     SURFACE_NO_CAM_COLLISION_77(0x0077), // Surface with no cam collision flag, unused
     SURFACE_NO_CAM_COL_VERY_SLIPPERY(0x0078), // Surface with no cam collision flag, very slippery with noise (THI)
     SURFACE_NO_CAM_COL_SLIPPERY(0x0079), // Surface with no cam collision flag, slippery with noise (CCM, PSS and TTM slides)
     SURFACE_SWITCH(0x007A), // Surface with no cam collision flag, non-slippery with noise, used by switches and Dorrie
     SURFACE_VANISH_CAP_WALLS(0x007B), // Vanish cap walls, pass through them with Vanish Cap
     SURFACE_PAINTING_WOBBLE_A6(0x00A6), // Painting wobble (BOB Left)
     SURFACE_PAINTING_WOBBLE_A7(0x00A7), // Painting wobble (BOB Middle)
     SURFACE_PAINTING_WOBBLE_A8(0x00A8), // Painting wobble (BOB Right)
     SURFACE_PAINTING_WOBBLE_A9(0x00A9), // Painting wobble (CCM Left)
     SURFACE_PAINTING_WOBBLE_AA(0x00AA), // Painting wobble (CCM Middle)
     SURFACE_PAINTING_WOBBLE_AB(0x00AB), // Painting wobble (CCM Right)
     SURFACE_PAINTING_WOBBLE_AC(0x00AC), // Painting wobble (WF Left)
     SURFACE_PAINTING_WOBBLE_AD(0x00AD), // Painting wobble (WF Middle)
     SURFACE_PAINTING_WOBBLE_AE(0x00AE), // Painting wobble (WF Right)
     SURFACE_PAINTING_WOBBLE_AF(0x00AF), // Painting wobble (JRB Left)
     SURFACE_PAINTING_WOBBLE_B0(0x00B0), // Painting wobble (JRB Middle)
     SURFACE_PAINTING_WOBBLE_B1(0x00B1), // Painting wobble (JRB Right)
     SURFACE_PAINTING_WOBBLE_B2(0x00B2), // Painting wobble (LLL Left)
     SURFACE_PAINTING_WOBBLE_B3(0x00B3), // Painting wobble (LLL Middle)
     SURFACE_PAINTING_WOBBLE_B4(0x00B4), // Painting wobble (LLL Right)
     SURFACE_PAINTING_WOBBLE_B5(0x00B5), // Painting wobble (SSL Left)
     SURFACE_PAINTING_WOBBLE_B6(0x00B6), // Painting wobble (SSL Middle)
     SURFACE_PAINTING_WOBBLE_B7(0x00B7), // Painting wobble (SSL Right)
     SURFACE_PAINTING_WOBBLE_B8(0x00B8), // Painting wobble (Unused - Left)
     SURFACE_PAINTING_WOBBLE_B9(0x00B9), // Painting wobble (Unused - Middle)
     SURFACE_PAINTING_WOBBLE_BA(0x00BA), // Painting wobble (Unused - Right)
     SURFACE_PAINTING_WOBBLE_BB(0x00BB), // Painting wobble (DDD - Left), makes the painting wobble if touched
     SURFACE_PAINTING_WOBBLE_BC(0x00BC), // Painting wobble (Unused, DDD - Middle)
     SURFACE_PAINTING_WOBBLE_BD(0x00BD), // Painting wobble (Unused, DDD - Right)
     SURFACE_PAINTING_WOBBLE_BE(0x00BE), // Painting wobble (WDW Left)
     SURFACE_PAINTING_WOBBLE_BF(0x00BF), // Painting wobble (WDW Middle)
     SURFACE_PAINTING_WOBBLE_C0(0x00C0), // Painting wobble (WDW Right)
     SURFACE_PAINTING_WOBBLE_C1(0x00C1), // Painting wobble (THI Tiny - Left)
     SURFACE_PAINTING_WOBBLE_C2(0x00C2), // Painting wobble (THI Tiny - Middle)
     SURFACE_PAINTING_WOBBLE_C3(0x00C3), // Painting wobble (THI Tiny - Right)
     SURFACE_PAINTING_WOBBLE_C4(0x00C4), // Painting wobble (TTM Left)
     SURFACE_PAINTING_WOBBLE_C5(0x00C5), // Painting wobble (TTM Middle)
     SURFACE_PAINTING_WOBBLE_C6(0x00C6), // Painting wobble (TTM Right)
     SURFACE_PAINTING_WOBBLE_C7(0x00C7), // Painting wobble (Unused, TTC - Left)
     SURFACE_PAINTING_WOBBLE_C8(0x00C8), // Painting wobble (Unused, TTC - Middle)
     SURFACE_PAINTING_WOBBLE_C9(0x00C9), // Painting wobble (Unused, TTC - Right)
     SURFACE_PAINTING_WOBBLE_CA(0x00CA), // Painting wobble (Unused, SL - Left)
     SURFACE_PAINTING_WOBBLE_CB(0x00CB), // Painting wobble (Unused, SL - Middle)
     SURFACE_PAINTING_WOBBLE_CC(0x00CC), // Painting wobble (Unused, SL - Right)
     SURFACE_PAINTING_WOBBLE_CD(0x00CD), // Painting wobble (THI Huge - Left)
     SURFACE_PAINTING_WOBBLE_CE(0x00CE), // Painting wobble (THI Huge - Middle)
     SURFACE_PAINTING_WOBBLE_CF(0x00CF), // Painting wobble (THI Huge - Right)
     SURFACE_PAINTING_WOBBLE_D0(0x00D0), // Painting wobble (HMC & COTMC - Left), makes the painting wobble if touched
     SURFACE_PAINTING_WOBBLE_D1(0x00D1), // Painting wobble (Unused, HMC & COTMC - Middle)
     SURFACE_PAINTING_WOBBLE_D2(0x00D2), // Painting wobble (Unused, HMC & COTMC - Right)
     SURFACE_PAINTING_WARP_D3(0x00D3), // Painting warp (BOB Left)
     SURFACE_PAINTING_WARP_D4(0x00D4), // Painting warp (BOB Middle)
     SURFACE_PAINTING_WARP_D5(0x00D5), // Painting warp (BOB Right)
     SURFACE_PAINTING_WARP_D6(0x00D6), // Painting warp (CCM Left)
     SURFACE_PAINTING_WARP_D7(0x00D7), // Painting warp (CCM Middle)
     SURFACE_PAINTING_WARP_D8(0x00D8), // Painting warp (CCM Right)
     SURFACE_PAINTING_WARP_D9(0x00D9), // Painting warp (WF Left)
     SURFACE_PAINTING_WARP_DA(0x00DA), // Painting warp (WF Middle)
     SURFACE_PAINTING_WARP_DB(0x00DB), // Painting warp (WF Right)
     SURFACE_PAINTING_WARP_DC(0x00DC), // Painting warp (JRB Left)
     SURFACE_PAINTING_WARP_DD(0x00DD), // Painting warp (JRB Middle)
     SURFACE_PAINTING_WARP_DE(0x00DE), // Painting warp (JRB Right)
     SURFACE_PAINTING_WARP_DF(0x00DF), // Painting warp (LLL Left)
     SURFACE_PAINTING_WARP_E0(0x00E0), // Painting warp (LLL Middle)
     SURFACE_PAINTING_WARP_E1(0x00E1), // Painting warp (LLL Right)
     SURFACE_PAINTING_WARP_E2(0x00E2), // Painting warp (SSL Left)
     SURFACE_PAINTING_WARP_E3(0x00E3), // Painting warp (SSL Medium)
     SURFACE_PAINTING_WARP_E4(0x00E4), // Painting warp (SSL Right)
     SURFACE_PAINTING_WARP_E5(0x00E5), // Painting warp (Unused - Left)
     SURFACE_PAINTING_WARP_E6(0x00E6), // Painting warp (Unused - Medium)
     SURFACE_PAINTING_WARP_E7(0x00E7), // Painting warp (Unused - Right)
     SURFACE_PAINTING_WARP_E8(0x00E8), // Painting warp (DDD - Left)
     SURFACE_PAINTING_WARP_E9(0x00E9), // Painting warp (DDD - Middle)
     SURFACE_PAINTING_WARP_EA(0x00EA), // Painting warp (DDD - Right)
     SURFACE_PAINTING_WARP_EB(0x00EB), // Painting warp (WDW Left)
     SURFACE_PAINTING_WARP_EC(0x00EC), // Painting warp (WDW Middle)
     SURFACE_PAINTING_WARP_ED(0x00ED), // Painting warp (WDW Right)
     SURFACE_PAINTING_WARP_EE(0x00EE), // Painting warp (THI Tiny - Left)
     SURFACE_PAINTING_WARP_EF(0x00EF), // Painting warp (THI Tiny - Middle)
     SURFACE_PAINTING_WARP_F0(0x00F0), // Painting warp (THI Tiny - Right)
     SURFACE_PAINTING_WARP_F1(0x00F1), // Painting warp (TTM Left)
     SURFACE_PAINTING_WARP_F2(0x00F2), // Painting warp (TTM Middle)
     SURFACE_PAINTING_WARP_F3(0x00F3), // Painting warp (TTM Right)
     SURFACE_TTC_PAINTING_1(0x00F4), // Painting warp (TTC Left)
     SURFACE_TTC_PAINTING_2(0x00F5), // Painting warp (TTC Medium)
     SURFACE_TTC_PAINTING_3(0x00F6), // Painting warp (TTC Right)
     SURFACE_PAINTING_WARP_F7(0x00F7), // Painting warp (SL Left)
     SURFACE_PAINTING_WARP_F8(0x00F8), // Painting warp (SL Middle)
     SURFACE_PAINTING_WARP_F9(0x00F9), // Painting warp (SL Right)
     SURFACE_PAINTING_WARP_FA(0x00FA), // Painting warp (THI Tiny - Left)
     SURFACE_PAINTING_WARP_FB(0x00FB), // Painting warp (THI Tiny - Middle)
     SURFACE_PAINTING_WARP_FC(0x00FC), // Painting warp (THI Tiny - Right)
     SURFACE_WOBBLING_WARP(0x00FD), // Pool warp (HMC & DDD)
     SURFACE_TRAPDOOR(0x00FF); // Bowser Left trapdoor, has no action defined

     public final byte value;

     private SM64SurfaceType(int value) {
          this.value = (byte)value;
     }

     public static byte getValue(SM64SurfaceType type) {
          return type.value;
     }

}
