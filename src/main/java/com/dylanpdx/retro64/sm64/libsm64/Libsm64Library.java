package com.dylanpdx.retro64.sm64.libsm64;
import com.dylanpdx.retro64.binExtract;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
public interface Libsm64Library extends Library {
	public static final String JNA_LIBRARY_NAME = binExtract.getLibPath().getAbsolutePath();
	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(Libsm64Library.JNA_LIBRARY_NAME);
	public static final Libsm64Library INSTANCE = (Libsm64Library)Native.loadLibrary(Libsm64Library.JNA_LIBRARY_NAME, Libsm64Library.class);
	public static final int SM64_TEXTURE_WIDTH = 64 * 11;
	public static final int SM64_TEXTURE_HEIGHT = 64;
	public static final int SM64_GEO_MAX_TRIANGLES = 2040;

	char SEQ_PLAYER_LEVEL=            0;  // Level background music
	char SEQ_PLAYER_ENV=              1;  // Misc music like the puzzle jingle
	char SEQ_PLAYER_SFX=              2;  // Sound effects
	public interface SM64DebugPrintFunctionPtr extends Callback {
		void apply(Pointer charPtr1);
	};

	void sm64_global_init(ByteBuffer rom,ByteBuffer outTexture, Libsm64Library.SM64DebugPrintFunctionPtr debugPrintFunction);
	void sm64_global_init_audioBin(ByteBuffer rom,ByteBuffer audioData,ByteBuffer outTexture, Libsm64Library.SM64DebugPrintFunctionPtr debugPrintFunction);
	void sm64_global_terminate();
	void sm64_static_surfaces_load(SM64Surface[] surfaceArray, int numSurfaces);
	int sm64_mChar_create(float x, float y, float z);
	void sm64_mChar_animTick(int mCharId, int stateFlags, AnimInfo info, SM64MCharGeometryBuffers outBuffers,int model,Pointer rotPointer);
	void sm64_mChar_tick(int mCharId, SM64MCharInputs inputs, SM64MCharState outState, SM64MCharGeometryBuffers outBuffers);
	void sm64_mChar_delete(int mCharId);
	void sm64_mChar_teleport(int mCharId, float x, float y, float z);
	void sm64_mChar_set_velocity(int mCharId, float x, float y, float z);
	void sm64_mChar_set_state(int mCharId, int capType);
	void sm64_mChar_set_water_level(int mCharId, int yLevel);
	void sm64_mChar_set_gas_level(int mCharId, int yLevel);
	int sm64_surface_object_create(SM64SurfaceObject surfaceObject);
	void sm64_surface_object_move(int objectId, SM64ObjectTransform transform);
	void sm64_surface_object_delete(int objectId);
	byte sm64_getSoundRequests(Pointer SM64SoundPtr1);
	AnimInfo sm64_get_anim_info(int mCharId,Pointer rot);
	void sm64_mChar_apply_damage(int mCharId, int damage, int interactionSubtype, float xSrc, float ySrc, float zSrc);
	void sm64_mChar_set_action(int mCharId, int actionId);
	void sm64_mChar_set_action_state(int mCharId, short actionId);
	void sm64_mChar_set_angle(int mCharId, float angle);
	void sm64_mChar_heal(int mCharId, byte healCounter);
	void sm64_seq_player_play_sequence(char player, char seqId, short arg2);
	void sm64_play_music(char player, short seqArgs, short fadeTimer);
	void sm64_stop_background_music(short seqId);
	void sm64_fadeout_background_music(short arg0, short fadeOut);
	short sm64_get_current_background_music();
	// extern SM64_LIB_FN void sm64_play_sound(s32 soundBits, f32 *pos);
	void sm64_play_sound_global(int soundBits);
	void sm64_set_volume(float volume);
	int sm64_get_version(); // returns version of the lib to use as a compatibility check
}
