package com.dylanpdx.retro64;

import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class surfaceItem {

    Vec3[] verts;
    SM64SurfaceType material;
    short terrain;
    boolean isCube;
    boolean isFlat;

    public surfaceItem(Vec3[] verts, SM64SurfaceType material, short terrain) {
        this.verts = verts;
        this.material = material;
        this.terrain = terrain;
    }

    public surfaceItem(List<Vec3> verts, SM64SurfaceType material, short terrain) {
        this.verts = new Vec3[verts.size()];
        for (int i = 0; i < verts.size(); i++) {
            this.verts[i] = verts.get(i);
        }
        this.material = material;
        this.terrain = terrain;
    }

    public surfaceItem(Vec3 pos, boolean isCube, boolean isFlat, SM64SurfaceType material, short terrain){
        verts = new Vec3[]{pos};
        this.isCube = isCube;
        this.isFlat = isFlat;
        this.material = material;
        this.terrain = terrain;
    }

    public boolean isCube(){
        return isCube;
    }

    public boolean isFlat(){
        return isFlat;
    }


}
