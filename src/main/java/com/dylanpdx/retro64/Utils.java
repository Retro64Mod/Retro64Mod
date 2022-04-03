package com.dylanpdx.retro64;

import com.dylanpdx.retro64.capabilities.smc64Capability;
import com.dylanpdx.retro64.capabilities.smc64CapabilityInterface;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * Get all Vertices of a block from it's BakedModel
     * @param bakedModel BakedModel of the block
     * @param blockState BlockState of the block
     * @param random Random number generator
     * @return List of Vec3 vertices
     */
    public static List<Vec3> getAllQuads(BakedModel bakedModel, BlockState blockState, Random random){
        ArrayList<Vec3> quads = new ArrayList<>();
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, null,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.DOWN,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.UP,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.EAST,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.NORTH,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.SOUTH,random)));
        quads.addAll(decodeQuads(bakedModel.getQuads(blockState, Direction.WEST,random)));
        return quads;
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


    public static double QuatToYaw(Quaternion q){
        // double yaw = Math.atan2(2.0*(q.j()*q.k() + q.r()*q.i()), q.r()*q.r() - q.i()*q.i() - q.j()*q.j() + q.k()*q.k());
        double pitch = Math.asin(-2.0*(q.i()*q.k() - q.r()*q.j()));
        // double roll = Math.atan2(2.0*(q.i()*q.j() + q.r()*q.k()), q.r()*q.r() + q.i()*q.i() - q.j()*q.j() - q.k()*q.k());
        return pitch;
    }

    /**
     * Rotate a point around a rotation
     * Based on https://github.com/Unity-Technologies/UnityCsReference/blob/master/Runtime/Export/Math/Quaternion.cs#L106
     * @param rotation Rotation to rotate around
     * @param point Point to rotate
     * @return Rotated point
     */
    public static Vec3 rotatePt(Quaternion rotation,Vec3 point){
        float x = rotation.i() * 2F;
        float y = rotation.j() * 2F;
        float z = rotation.k() * 2F;
        float xx = rotation.i() * x;
        float yy = rotation.j() * y;
        float zz = rotation.k() * z;
        float xy = rotation.i() * y;
        float xz = rotation.j() * z;
        float yz = rotation.k() * z;
        float wx = rotation.r() * x;
        float wy = rotation.r() * y;
        float wz = rotation.r() * z;

        Vec3 res = new Vec3(
                (1F - (yy + zz)) * point.x + (xy - wz) * point.y + (xz + wy) * point.z,
                (xy + wz) * point.x + (1F - (xx + zz)) * point.y + (yz - wx) * point.z,
                (xz - wy) * point.x + (yz + wx) * point.y + (1F - (xx + yy)) * point.z);
        return res;
    }

    public static smc64CapabilityInterface getSmc64Capability(Player player){
        return player.getCapability(smc64Capability.INSTANCE).map(smc64->{
            return smc64;
        }).orElse(null);
    }

    public static Quaternion lookAt(Vector3f from,Vector3f to){
        Vector3f dir = new Vector3f(to.x()-from.x(),to.y()-from.y(),to.z()-from.z());
        dir.normalize();
        Vector3f right = new Vector3f(0,1,0);right.cross(dir);
        Vector3f up = new Vector3f(dir.x(),dir.y(),dir.z());up.cross(right);
        Matrix4f matrix = new Matrix4f(
                new float[]{
                        right.x(), right.y(), right.z(), from.x(),
                        up.x(), up.y(), up.z(), from.y(),
                        dir.x(), dir.y(), dir.z(), from.z(),
                        0, 0, 0, 1
                }
        );

        var quat = matrixToQuaternion(right.x(),right.y(),right.z(),up.x(),up.y(),up.z(), dir.x(), dir.y(), dir.z());
        return quat;
    }

    public static float[] lookAtPitchYaw(Vector3f from,Vector3f to){
        Vector3f dir = new Vector3f(to.x()-from.x(),to.y()-from.y(),to.z()-from.z());
        dir.normalize();
        var yaw = Math.atan(dir.x()/-dir.y());
        var pitch = Math.atan(Math.sqrt(dir.x()*dir.x()+dir.y()*dir.y())/dir.z());
        return new float[]{(float) pitch, (float) yaw};
    }

    public static Quaternion matrixToQuaternion(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22){
        float trace = m00 + m11 + m22;
        float s;
        float x;
        float y;
        float z;
        float w;
        if (trace > 0) {
            s = 0.5f / (float) Math.sqrt(trace + 1.0f);
            w = 0.25f / s;
            x = (m12 - m21) * s;
            y = (m20 - m02) * s;
            z = (m01 - m10) * s;
        } else {
            if (m00 > m11 && m00 > m22) {
                s = (float) Math.sqrt(1.0f + m00 - m11 - m22) * 2.0f;
                w = (m01 + m10) / s;
                x = 0.25f * s;
                y = (m20 + m02) / s;
                z = (m12 - m21) / s;
            } else if (m11 > m22) {
                s = (float) Math.sqrt(1.0f + m11 - m00 - m22) * 2.0f;
                w = (m12 + m21) / s;
                x = (m01 + m10) / s;
                y = 0.25f * s;
                z = (m20 - m02) / s;
            } else {
                s = (float) Math.sqrt(1.0f + m22 - m00 - m11) * 2.0f;
                w = (m20 + m02) / s;
                x = (m12 + m21) / s;
                y = (m01 + m10) / s;
                z = 0.25f * s;
            }
        }
        return new Quaternion(x,y,z,w);
    }

    public static DataInputStream dataStreamAtPos(byte[] data, int pos, int length){
        var truncated = new byte[length];
        System.arraycopy(data,pos,truncated,0,length);
        return new DataInputStream(new ByteArrayInputStream(truncated));
    }
    public static DataInputStream dataStreamAtPos(byte[] data, int pos){
        return dataStreamAtPos(data,pos,data.length-pos);
    }

    /**
     * Makes a Quaternion that looks at a rotation
     * Based on https://answers.unity.com/questions/467614/what-is-the-source-code-of-quaternionlookrotation.html
     * @param forward Vector to look at
     * @param up Vector to use as up
     * @return Quaternion that looks at the rotation
     */
    public static Quaternion QuaternionLookRotation(Vec3 forward, Vec3 up)
    {
        forward.normalize();

        Vec3 vector = forward.normalize();
        Vec3 vector2 = up.cross(vector).normalize();
        Vec3 Vec3 = vector.cross(vector2);
        var m00 = (float)vector2.x;
        var m01 = (float)vector2.y;
        var m02 = (float)vector2.z;
        var m10 = (float)Vec3.x;
        var m11 = (float)Vec3.y;
        var m12 = (float)Vec3.z;
        var m20 = (float)vector.x;
        var m21 = (float)vector.y;
        var m22 = (float)vector.z;


        double num8 = (m00 + m11) + m22;
        var quaternion = Quaternion.fromYXZ(0,0,0);
        if (num8 > 0f)
        {
            // I J K R
            // X Y Z W
            var num = (float)Math.sqrt(num8 + 1f);
            quaternion.set(quaternion.i(),quaternion.j(),quaternion.k(),num * 0.5f);;
            num = 0.5f / num;
            quaternion.set((m12 - m21) * num,(m20 - m02) * num,(m01 - m10) * num,quaternion.r());
            return quaternion;
        }
        if ((m00 >= m11) && (m00 >= m22))
        {
            var num7 = (float)Math.sqrt(((1f + m00) - m11) - m22);
            var num4 = 0.5f / num7;
            quaternion.set(0.5f * num7,(m01 + m10) * num4,(m02 + m20) * num4,(m12 - m21) * num4);
            return quaternion;
        }
        if (m11 > m22)
        {
            var num6 = (float)Math.sqrt(((1f + m11) - m00) - m22);
            var num3 = 0.5f / num6;
            quaternion.set((m10 + m01) * num3,0.5f * num6,(m21 + m12) * num3,(m20 - m02) * num3);
            //quaternion.x = (m10+ m01) * num3;
            //quaternion.y = 0.5f * num6;
            //quaternion.z = (m21 + m12) * num3;
            //quaternion.w = (m20 - m02) * num3;
            return quaternion;
        }
        var num5 = (float)Math.sqrt(((1f + m22) - m00) - m11);
        var num2 = 0.5f / num5;
        quaternion.set((m20 + m02) * num2,(m21 + m12) * num2,0.5f * num5,(m01 - m10) * num2);
        //quaternion.x = (m20 + m02) * num2;
        //quaternion.y = (m21 + m12) * num2;
        //quaternion.z = 0.5f * num5;
        //quaternion.w = (m01 - m10) * num2;
        return quaternion;
    }

    public static Quaternion QuaternionLookRotation(Vector3f forward, Vector3f up){
        return QuaternionLookRotation(new Vec3(forward.x(),forward.y(),forward.z()),new Vec3(up.x(),up.y(),up.z()));
    }
}
