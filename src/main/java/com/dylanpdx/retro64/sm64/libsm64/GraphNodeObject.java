package com.dylanpdx.retro64.sm64.libsm64;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;

public class GraphNodeObject extends Structure {
    public GraphNode.ByValue node;
    public GraphNode sharedChild;
    public char areaIndex;
    public char activeAreaIndex;
    public float[] angle = new float[3];
    public float[] pos = new float[3];
    public float[] scale = new float[3];
    public AnimInfo.ByValue animInfo;
    public Pointer unk4C; // SpawnInfo
    public float[] throwMatrix = new float[4*4];
    public float[] cameraToObject= new float[3];

    @Override
    protected List<String> getFieldOrder() {
        return List.of("node", "sharedChild", "areaIndex", "activeAreaIndex", "angle", "pos", "scale", "animInfo", "unk4C", "throwMatrix", "cameraToObject");
    }
}

class GraphNode extends Structure{

    short type;
    short flags;
    GraphNode prev;
    GraphNode next;
    GraphNode parent;
    GraphNode children;

    @Override
    protected List<String> getFieldOrder() {
        return List.of("type", "flags", "prev", "next", "parent", "children");
    }

    public static class ByValue extends GraphNode implements Structure.ByValue {

    };
}