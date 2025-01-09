package com.dylanpdx.retro64;

import com.dylanpdx.retro64.capabilities.smc64Capability;
import com.dylanpdx.retro64.capabilities.smc64CapabilityInterface;
import com.dylanpdx.retro64.maps.BlockMatMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Find Water level at the current position
     * @param world Minecraft world
     * @param pos Block position
     * @return Water level
     */
    public static int findWaterLevel(ClientLevel world, BlockPos pos){
        int lvl = -100;
        for (int i = pos.getY();i<255;i++){
            if (world.isWaterAt(pos.atY(i)))
                lvl=i;
            else
                return lvl;
        }
        return -100;
    }

    /**
     * Turn a list of BakedQuads into a list of Vec3 vertices
     * @param quads List of BakedQuads
     * @return List of Vec3 vertices
     */
    public static List<Vec3> decodeQuads(List<BakedQuad> quads){
        ArrayList<Vec3> pquads = new ArrayList<>();
        for (BakedQuad quad : quads){
            for (Vec3 pos : decodeQuad(quad)){
                pquads.add(pos);
            }
        }
        return pquads;
    }

    /**
     * Take a single BakedQuad and turn it into an array of Vec3 vertices
     * @param quad BakedQuad to decode
     * @return List of Vec3 vertices
     */
    public static Vec3[] decodeQuad(BakedQuad quad){
        int[] vertexData=quad.getVertices();
        Vec3[] dquad = new Vec3[4];
        for (int i = 0;i<4;i++){
            float x = fFromByteArray(iToByteArray(vertexData[8 * i]));
            float y = fFromByteArray(iToByteArray(vertexData[8 * i+1]));
            float z = fFromByteArray(iToByteArray(vertexData[8 * i+2]));
            dquad[i]=new Vec3(x,y,z);
        }
        return dquad;
    }

    /**
     * Get all Vertices of a block from its BakedModel
     * @param bakedModel BakedModel of the block
     * @param blockState BlockState of the block
     * @param random Random number generator
     * @return List of Vec3 vertices
     */
    public static List<Vec3> getQuadsFromModel(BakedModel bakedModel, BlockState blockState, RandomSource random, boolean passThrough){
        if (passThrough) return new ArrayList<>();

        ArrayList<Vec3> quads = new ArrayList<>();
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, null, random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.DOWN,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.UP,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.EAST,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.NORTH,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.SOUTH,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.WEST,random)));
        return quads;
    }

    /**
     * Get all Vertices of a block from its Collision (Block Hitbox)
     * @param world World the block is in
     * @param pos Position of that block
     * @return List of Vec3 vertices
     */
    public static List<Vec3> getQuadsFromHitbox(ClientLevel world, BlockPos pos, boolean passThrough){
        // Using this instead of the defined surface type, this makes it more customizable
        if (passThrough) return new ArrayList<>();

        BlockState blockState = world.getBlockState(pos.immutable());

        boolean maxHitbox = blockState.getTags().anyMatch(t -> t == BlockMatMaps.maxHitbox);

        List<Vec3> vecList = new ArrayList<>();

        double trueMinX = 0;
        double trueMinY = 0;
        double trueMinZ = 0;
        double trueMaxX = 0;
        double trueMaxY = 0;
        double trueMaxZ = 0;

        for (AABB parts : blockState.getCollisionShape(world, pos).toAabbs()){
            if (maxHitbox) {
                trueMinX = Math.min(trueMinX, parts.minX);
                trueMinY = Math.min(trueMinY, parts.minY);
                trueMinZ = Math.min(trueMinZ, parts.minZ);
                trueMaxX = Math.max(trueMaxX, parts.maxX);
                trueMaxY = Math.max(trueMaxY, parts.maxY);
                trueMaxZ = Math.max(trueMaxZ, parts.maxZ);
            } else {
                vecList.addAll(AABBToVec3List(parts));
            }
        }

        // System.out.println(trueMinX + " " + trueMinY + " " + trueMinZ + " " + trueMaxX + " " + trueMaxY + " " + trueMaxZ);

        if (maxHitbox) return createVectors(trueMinX, trueMinY, trueMinZ, trueMaxX, trueMaxY, trueMaxZ); else return vecList;
    }

    public static List<Vec3> AABBToVec3List(AABB aabb){
        return createVectors(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    private static List<Vec3> createVectors(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
        // The order is very important, otherwise it breaks

        List<Vec3> vecters = new ArrayList<>();

        // Bottom
        vecters.add(new Vec3(minX, minY, maxZ));
        vecters.add(new Vec3(minX, minY, minZ));
        vecters.add(new Vec3(maxX, minY, minZ));
        vecters.add(new Vec3(maxX, minY, maxZ));
        // Top
        vecters.add(new Vec3(minX, maxY, minZ));
        vecters.add(new Vec3(minX, maxY, maxZ));
        vecters.add(new Vec3(maxX, maxY, maxZ));
        vecters.add(new Vec3(maxX, maxY, minZ));
        // East
        vecters.add(new Vec3(maxX, maxY, maxZ));
        vecters.add(new Vec3(maxX, minY, maxZ));
        vecters.add(new Vec3(maxX, minY, minZ));
        vecters.add(new Vec3(maxX, maxY, minZ));
        // North
        vecters.add(new Vec3(maxX, maxY, minZ));
        vecters.add(new Vec3(maxX, minY, minZ));
        vecters.add(new Vec3(minX, minY, minZ));
        vecters.add(new Vec3(minX, maxY, minZ));
        // South
        vecters.add(new Vec3(minX, maxY, maxZ));
        vecters.add(new Vec3(minX, minY, maxZ));
        vecters.add(new Vec3(maxX, minY, maxZ));
        vecters.add(new Vec3(maxX, maxY, maxZ));
        // West
        vecters.add(new Vec3(minX, maxY, minZ));
        vecters.add(new Vec3(minX, minY, minZ));
        vecters.add(new Vec3(minX, minY, maxZ));
        vecters.add(new Vec3(minX, maxY, maxZ));

        return vecters;
    }

    static byte[] iToByteArray(int value) {
        return  ByteBuffer.allocate(4).putInt(value).array();
    }

    static float fFromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static int iFromByteArray(byte[] bytes) {
        return  ByteBuffer.wrap(bytes).getInt();
    }

    public static smc64CapabilityInterface getSmc64Capability(Player player){
        return player.getCapability(smc64Capability.INSTANCE).map(smc64->{
            return smc64;
        }).orElse(null);
    }

    public static boolean getIsMario(Player player){
        var cap = getSmc64Capability(player);
        if (cap==null) return false;
        return cap.getIsEnabled();
    }

    public static void setIsMario(Player player, boolean value){
        var cap = getSmc64Capability(player);
        if (cap==null) return;
        cap.setIsEnabled(value);
    }

    public static DataInputStream dataStreamAtPos(byte[] data, int pos, int length){
        var truncated = new byte[length];
        System.arraycopy(data,pos,truncated,0,length);
        return new DataInputStream(new ByteArrayInputStream(truncated));
    }
    public static DataInputStream dataStreamAtPos(byte[] data, int pos){
        return dataStreamAtPos(data,pos,data.length-pos);
    }

    public static String getRegistryName(Block block){
        return BuiltInRegistries.BLOCK.getKey(block).toString();
    }

    public static String getRegistryName(Item item){
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    public static String getRegistryName(MobEffect effect){
        return BuiltInRegistries.MOB_EFFECT.getKey(effect).toString();
    }

    public static String getRegistryName(SoundEvent sound){
        return BuiltInRegistries.SOUND_EVENT.getKey(sound).toString();
    }

    public static Quaternionf quaternionFromXYZ(float x,float y,float z){
        return new Quaternionf().identity().rotateXYZ(x,y,z);
    }

    public static Vector3f vec3toVector3f(Vec3 v3){
        return new Vector3f((float) v3.x,(float)v3.y,(float)v3.z);
    }

    public static boolean isConnectedToVanillaServer(){
        return NetworkHooks.isVanillaConnection(Minecraft.getInstance().player.connection.getConnection());
    }
}
