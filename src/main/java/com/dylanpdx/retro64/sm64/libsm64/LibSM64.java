package com.dylanpdx.retro64.sm64.libsm64;

import com.dylanpdx.retro64.*;
import com.dylanpdx.retro64.sm64.SM64MCharAction;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static com.dylanpdx.retro64.sm64.libsm64.Libsm64Library.SM64_TEXTURE_HEIGHT;
import static com.dylanpdx.retro64.sm64.libsm64.Libsm64Library.SM64_TEXTURE_WIDTH;

public class LibSM64 {

    public static final float SCALE_FACTOR = 110;

    static boolean isGlobalInit;

    static final int supportedVer=5;

    public final int SM64_MAX_HEALTH = 8;

    public static class mCharState
    {
        static int
        MCHAR_NORMAL_CAP = 0x00000001,
        MCHAR_VANISH_CAP = 0x00000002,
        MCHAR_METAL_CAP = 0x00000004,
        MCHAR_WING_CAP = 0x00000008,
        MCHAR_CAP_ON_HEAD = 0x00000010,
        MCHAR_CAP_IN_HAND = 0x00000020,
        MCHAR_METAL_SHOCK = 0x00000040,
        MCHAR_TELEPORTING = 0x00000080,
        MCHAR_UNKNOWN_08 = 0x00000100,
        MCHAR_UNKNOWN_13 = 0x00002000,
        MCHAR_ACTION_SOUND_PLAYED = 0x00010000,
        MCHAR_MCHAR_SOUND_PLAYED = 0x00020000,
        MCHAR_UNKNOWN_18 = 0x00040000,
        MCHAR_PUNCHING = 0x00100000,
        MCHAR_KICKING = 0x00200000,
        MCHAR_TRIPPING = 0x00400000,
        MCHAR_UNKNOWN_25 = 0x02000000,
        MCHAR_UNKNOWN_30 = 0x40000000,
        MCHAR_UNKNOWN_31 = 0x80000000;
    }

    public static void GlobalInit(String romPath) throws IOException {
        // read all bytes from file at romPath
        File f = new File(romPath);
        byte[] romData = Files.readAllBytes(f.toPath());
        GlobalInit(romData);

        ArrayList<SM64Surface> surfs = new ArrayList<>();
        surfs.addAll(Arrays.asList(LibSM64SurfUtils.block(0, 4, 0)));
        surfs.addAll(Arrays.asList(LibSM64SurfUtils.block(-1, 4, -1)));
        surfs.addAll(Arrays.asList(LibSM64SurfUtils.block(-1, 4, 0)));
        surfs.addAll(Arrays.asList(LibSM64SurfUtils.block(0, 4, -1)));
        surfs.addAll(Arrays.asList(LibSM64SurfUtils.block(1, 4, 1)));
        LibSM64.StaticSurfacesLoad(surfs.toArray(new SM64Surface[0]));
    }

    public static void GlobalInit(byte[] rom) throws IOException {
        ByteBuffer textureData = ByteBuffer.allocate(4 * SM64_TEXTURE_WIDTH * SM64_TEXTURE_HEIGHT);
        Retro64.LOGGER.info("GlobalInit");
        Libsm64Library.INSTANCE.sm64_global_init(
                ByteBuffer.wrap(rom),textureData,null);
        Retro64.LOGGER.info("GlobalInit done");
        genTextureData(textureData);

        isGlobalInit = true;
    }

    private static void genTextureData(ByteBuffer textureData) {
        NativeImage image = new NativeImage(SM64_TEXTURE_WIDTH, SM64_TEXTURE_HEIGHT, false);
        for (int ix = 0; ix < SM64_TEXTURE_WIDTH; ix++)
        {
            for (int iy = 0; iy < SM64_TEXTURE_HEIGHT; iy++)
            {
                image.setPixelRGBA(ix,iy, Utils.iFromByteArray(new byte[]{
                        textureData.get(4 * (ix + SM64_TEXTURE_WIDTH * iy) + 3),
                        textureData.get(4 * (ix + SM64_TEXTURE_WIDTH * iy) + 2),
                        textureData.get(4 * (ix + SM64_TEXTURE_WIDTH * iy) + 1),
                        textureData.get(4 * (ix + SM64_TEXTURE_WIDTH * iy) + 0)
                }));
            }
        }
        var mtex=TexGenerator.convertRawTexToMChar(image);
        textureManager.setMCharTexture(mtex);
    }

    public static void GlobalTerminate(){
        Libsm64Library.INSTANCE.sm64_global_terminate();
        isGlobalInit = false;
    }

    public static void StaticSurfacesLoad(SM64Surface[] surfaces){
        if (surfaces.length==0)
            return;
        SM64Surface[] surfs = (SM64Surface[]) new SM64Surface().toArray(surfaces.length);
        for (int i = 0; i < surfaces.length; i++)
        {
            surfs[i].force = surfaces[i].force;
            surfs[i].terrain= surfaces[i].terrain;
            surfs[i].type = surfaces[i].type;
            surfs[i].vertices= surfaces[i].vertices;
        }
        Libsm64Library.INSTANCE.sm64_static_surfaces_load(surfs, surfaces.length); // may not work due to JNA
    }

    public static int MCharCreate(Vector3f pos){
        pos = PUFixer.convertToSM64(pos);
        return Libsm64Library.INSTANCE.sm64_mChar_create(pos.x(),pos.y(),pos.z());
    }


    public static void MCharTick(int mCharId, SM64MCharInputs inputs, SM64MCharState state,SM64MCharGeometryBuffers geometryBuffers){
        Libsm64Library.INSTANCE.sm64_mChar_tick(mCharId,inputs,state,geometryBuffers);

    }

    public static void MCharDelete(int mCharId){
        Libsm64Library.INSTANCE.sm64_mChar_delete(mCharId);
    }

    public static void MCharTeleport(int mCharId, Vec3 pos){
        Libsm64Library.INSTANCE.sm64_mChar_teleport(mCharId,(float)pos.x()*SCALE_FACTOR,(float)pos.y()*SCALE_FACTOR,(float)pos.z()*SCALE_FACTOR);
    }

    public static void MCharChangeState(int mCharId, int stateID){
        Libsm64Library.INSTANCE.sm64_mChar_set_state(mCharId,stateID);
    }

    public static void MCharChangeAction(int mCharId, SM64MCharAction action){
        MCharChangeAction(mCharId,action.id);
    }

    public static void MCharChangeAction(int mCharId, int actionID){
        Libsm64Library.INSTANCE.sm64_mChar_set_action(mCharId,actionID);
    }

    public static void MCharSetWaterLevel(int mCharId, int waterLevel){
        Libsm64Library.INSTANCE.sm64_mChar_set_water_level(mCharId,waterLevel);
    }

    public static void SurfaceObjectDelete(int surfaceObjectId){
        Libsm64Library.INSTANCE.sm64_surface_object_delete(surfaceObjectId);
    }

    static short SEQUENCE_ARGS(int priority,int seqId){
        return (short) ((priority << 8) | seqId);
    }

    public static void playMusic(int seqId){
        Libsm64Library.INSTANCE.sm64_play_music(Libsm64Library.SEQ_PLAYER_LEVEL,SEQUENCE_ARGS(15,seqId),(short)0);
    }

    public static void playShellMusic(){
        Libsm64Library.INSTANCE.sm64_play_music(Libsm64Library.SEQ_PLAYER_LEVEL,SEQUENCE_ARGS(4,SeqId.SEQ_EVENT_POWERUP.getValue() | SeqId.SEQ_VARIATION),(short)0);
    }

    public static void playSoundGlobal(SM64Sounds sound){
        Libsm64Library.INSTANCE.sm64_play_sound_global(sound.getBits());
    }


    public static int getVersion(){
        if (!libFileExists())
            return 0;
        return Libsm64Library.INSTANCE.sm64_get_version();
    }

    public static boolean isSupportedVersion(){
        return getVersion()==supportedVer;
    }

    public static boolean libFileExists(){
        Retro64.LOGGER.info("Checking for libsm64 library");
        return binExtract.getLibPath()!=null;
    }

}
