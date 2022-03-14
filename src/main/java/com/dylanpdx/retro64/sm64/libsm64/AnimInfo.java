package com.dylanpdx.retro64.sm64.libsm64;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class AnimInfo extends Structure {
    public short animID;
    public short animYTrans;
    public Pointer curAnim; // *curAnim; unused
    public short animFrame;
    public short animTimer; // unsigned
    public int animFrameAccelAssist;
    public int animAccel;

    public AnimInfo(){
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("animID", "animYTrans", "curAnim", "animFrame", "animTimer", "animFrameAccelAssist", "animAccel");
    }

    public AnimInfo(short animID, short animYTrans, short animFrame, short animTimer, short animFrameAccelAssist, short animAccel) {
        super();
        this.animID = animID;
        this.animYTrans = animYTrans;
        this.animFrame = animFrame;
        this.animTimer = animTimer;
        this.animFrameAccelAssist = animFrameAccelAssist;
        this.animAccel = animAccel;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(this.animID);
        dos.writeShort(this.animYTrans);
        dos.writeShort(this.animFrame);
        dos.writeShort(this.animTimer);
        dos.writeInt(this.animFrameAccelAssist);
        dos.writeInt(this.animAccel);
        dos.close();
        return baos.toByteArray();
    }

    public static AnimInfo deserialize(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        AnimInfo animInfo = new AnimInfo();
        animInfo.animID = dis.readShort();
        animInfo.animYTrans = dis.readShort();
        animInfo.animFrame = dis.readShort();
        animInfo.animTimer = dis.readShort();
        animInfo.animFrameAccelAssist = dis.readInt();
        animInfo.animAccel = dis.readInt();
        dis.close();
        return animInfo;
    }

    public static class ByValue extends AnimInfo implements Structure.ByValue {

    };
}
