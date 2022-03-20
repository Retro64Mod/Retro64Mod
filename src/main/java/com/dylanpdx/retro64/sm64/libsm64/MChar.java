package com.dylanpdx.retro64.sm64.libsm64;

import com.dylanpdx.retro64.Retro64;
import com.dylanpdx.retro64.SM64EnvManager;
import com.dylanpdx.retro64.sm64.SM64MCharAction;
import com.mojang.math.Vector3f;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class MChar {

    public int id=-1;
    public SM64MCharState state;
    public AnimInfo animInfo;
    public double deathTime;

    SM64MCharGeometryBuffers outGeometry;
    public SM64MCharInputs inputs;
    public short animXRot,animYRot,animZRot;
    Pointer rotPointer;

    float[] geomPos;
    float[] geomNorms;
    float[] geomColors;
    float[] geomUVs;

    /**
     * Debug - Export current MChar model to OBJ file
     * @param filename - filename to export to
     */
    public void exportOBJ(String filename){
        int numTriangles=geomPos.length/9;
        StringBuilder sb=new StringBuilder();
        for (int i = 0;i<geomPos.length;i+=3){
            sb.append("v ").append(geomPos[i]).append(" ").append(geomPos[i+1]).append(" ").append(geomPos[i+2]).append("\n");
        }
        for (int i = 0;i<geomUVs.length;i+=2){
            sb.append("vt ").append(geomUVs[i]).append(" ").append(geomUVs[i+1]).append("\n");

        }

        for (int i=0;i<numTriangles;i++){
            sb.append("f ");
            for (int j=0;j<3;j++){
                sb.append((i*3+j+1)+"/");
                sb.append((i*3+j+1)+" ");
            }
            sb.append("\n");
        }
        String obj=sb.toString();
        try {
            java.io.FileWriter fw=new java.io.FileWriter(filename);
            fw.write(obj);
            fw.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public MChar(){
        SM64EnvManager.updateSurfs(null);
        id = LibSM64.MCharCreate(new Vector3f(Minecraft.getInstance().player.position()));
        if (id==-1){
            Retro64.LOGGER.info("MChar creation failed");
            return;
        }
        state = new SM64MCharState();
        inputs= new SM64MCharInputs();
        outGeometry= new SM64MCharGeometryBuffers();
        outGeometry.position = allocateVector3Array(Libsm64Library.SM64_GEO_MAX_TRIANGLES*3); // 3 vector3f per triangle
        outGeometry.normal = allocateVector3Array(Libsm64Library.SM64_GEO_MAX_TRIANGLES*3); // 3 vector3f per triangle
        outGeometry.color = allocateVector3Array(Libsm64Library.SM64_GEO_MAX_TRIANGLES*3); // 3 vector3f per triangle
        outGeometry.uv = allocateVector2Array(Libsm64Library.SM64_GEO_MAX_TRIANGLES*3); // 3 vector2f per triangle
        rotPointer=new Memory(3*2); // 3 shorts, 2 bytes per short

        fixedUpdate();

    }

    public void destroy(){
        Libsm64Library.INSTANCE.sm64_mChar_delete(id);
    }

    public void fixedUpdate(){

        LibSM64.MCharTick(id,inputs,state,outGeometry);
        if (y()<-70)
            damage(8,Vec3.ZERO);
        geomPos=outGeometry.position.getFloatArray(0,outGeometry.numTrianglesUsed*9);
        geomNorms=outGeometry.normal.getFloatArray(0,outGeometry.numTrianglesUsed*9);
        geomColors=outGeometry.color.getFloatArray(0,outGeometry.numTrianglesUsed*9);
        geomUVs=outGeometry.uv.getFloatArray(0,outGeometry.numTrianglesUsed*2*3);
        for (int i=0;i<geomPos.length;i++){
            geomPos[i]/=LibSM64.SCALE_FACTOR;
        }
        animInfo = Libsm64Library.INSTANCE.sm64_get_anim_info(id,rotPointer);
        animXRot=rotPointer.getShort(0);
        animYRot=rotPointer.getShort(2);
        animZRot=rotPointer.getShort(4);
        if ((state.health==255 || state.action== SM64MCharAction.ACT_QUICKSAND_DEATH.id) && deathTime==0){
            deathTime= Util.getEpochMillis();
        }
    }

    public void animUpdate(){
        rotPointer.setShort(0,animXRot);
        rotPointer.setShort(2,animYRot);
        rotPointer.setShort(4,animZRot);
        Libsm64Library.INSTANCE.sm64_mChar_animTick(id, LibSM64.mCharState.MCHAR_NORMAL_CAP | LibSM64.mCharState.MCHAR_CAP_ON_HEAD, animInfo,outGeometry,state.currentModel,
                rotPointer);
        //fixedUpdate();
        geomPos=outGeometry.position.getFloatArray(0,outGeometry.numTrianglesUsed*9);
        geomNorms=outGeometry.normal.getFloatArray(0,outGeometry.numTrianglesUsed*9);
        geomColors=outGeometry.color.getFloatArray(0,outGeometry.numTrianglesUsed*9);
        geomUVs=outGeometry.uv.getFloatArray(0,outGeometry.numTrianglesUsed*2*3);
        for (int i=0;i<geomPos.length;i++){
            geomPos[i]/=LibSM64.SCALE_FACTOR;
        }
    }

    public float[] getVertices(){
        return geomPos;
    }

    public float[] getNormals(){
        return geomNorms;
    }

    public float[] getColors(){
        return geomColors;
    }

    public float[] getUVs(){
        return geomUVs;
    }

    public float x(){
        return state.position[0]/LibSM64.SCALE_FACTOR;
    }

    public float y(){
        return state.position[1]/LibSM64.SCALE_FACTOR;
    }

    public float z(){
        return state.position[2]/LibSM64.SCALE_FACTOR;
    }

    public void teleport(Vec3 pos){
        LibSM64.MCharTeleport(id,pos);
    }

    public void damage(int amount,Vec3 pos){
        Libsm64Library.INSTANCE.sm64_mChar_apply_damage(id,amount,0,(float)pos.x(),(float)pos.y(),(float)pos.z());
    }

    static Pointer allocateVector3Array(int size){
        long bsize=4L *3*size; // 3 floats, 4 bytes per float
        Memory m = new Memory(bsize);
        m.clear();
        return m;
    }

    static Pointer allocateVector2Array(int size){
        long bsize=4L *2*size; // 2 floats, 4 bytes per float
        Memory m = new Memory(bsize);
        m.clear();
        return m;
    }

    public Vector3f velocity(){
        Vector3f v = new Vector3f(state.velocity[0],state.velocity[1],state.velocity[2]);
        return PUFixer.convertToMC(v);
    }

    public void setVelocity(Vector3f v){
        var converted=PUFixer.convertToSM64(v);
        Libsm64Library.INSTANCE.sm64_mChar_set_velocity(id,converted.x(),converted.y(),converted.z());
    }

    public float velocityMagnitude(){
        var velocity = velocity();
        return (float) Math.sqrt(velocity.x()*velocity.x()+/*velocity.y()*velocity.y()+*/velocity.z()*velocity.z());
    }

    public void heal(byte amount){
        Libsm64Library.INSTANCE.sm64_mChar_heal(id,(byte)(amount));
    }

}
