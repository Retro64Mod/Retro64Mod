package com.dylanpdx.retro64.sm64.libsm64;

import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;

public class LibSM64SurfUtils {
    public static SM64Surface generateTri(Vector3f point1, Vector3f point2, Vector3f point3, Vector3f translation, short surface, short terrain)
    {
        //translation = translation + new Vector3f(0, -1, 0);
        Vector3f cp1 = point1.copy();
        Vector3f cp2 = point2.copy();
        Vector3f cp3 = point3.copy();
        cp1.add(translation);
        cp2.add(translation);
        cp3.add(translation);
        cp1 = PUFixer.convertToSM64(cp1);
        cp2 = PUFixer.convertToSM64(cp2);
        cp3 = PUFixer.convertToSM64(cp3);

        SM64Surface surf = new SM64Surface();
        surf.force=0;
        surf.type = surface;
        surf.terrain = terrain;
        // tri 1
        surf.vertices[0] = (int)cp1.x();
        surf.vertices[1] = (int)cp1.y();
        surf.vertices[2] = (int)cp1.z();
        // tri 2
        surf.vertices[3] = (int)cp2.x();
        surf.vertices[4] = (int)cp2.y();
        surf.vertices[5] = (int)cp2.z();
        // tri 3
        surf.vertices[6] = (int)cp3.x();
        surf.vertices[7] = (int)cp3.y();
        surf.vertices[8] = (int)cp3.z();
        return surf;
    }

    public static SM64Surface[] generateQuad(Vector3f a, Vector3f b, Vector3f c, Vector3f d, Vector3f translation, short surface, short terrain)
    {
        return new SM64Surface[]
                {
                        generateTri(a,b,d, translation,surface,terrain),
                        generateTri(d, b, c, translation,surface,terrain)
                };
    }

    public static SM64Surface[] generateQuadF(Vector3f a, Vector3f b, Vector3f c, Vector3f d, Vector3f translation, short surface, short terrain)
    {
        return new SM64Surface[]
                {
                        generateTri(d, b, a, translation,surface,terrain),
                        generateTri(d, c, b, translation,surface,terrain)
                };
    }

    public static SM64Surface[] block(int x,int y,int z){
        return block(x,y,z,1.1f,-0.1f,(short) SM64SurfaceType.SURFACE_DEFAULT.value,(short)0);
    }

    public static Vec3[] surfToVec(SM64Surface surf){
        Vec3[] ret = new Vec3[3];
        ret[0] = new Vec3(surf.vertices[0],surf.vertices[1],surf.vertices[2]);
        ret[1] = new Vec3(surf.vertices[3],surf.vertices[4],surf.vertices[5]);
        ret[2] = new Vec3(surf.vertices[6],surf.vertices[7],surf.vertices[8]);
        return ret;
    }

    public static Vec3[] surfsToVec(SM64Surface[] surfs){
        Vec3[] ret = new Vec3[surfs.length*3];
        for(int i=0;i<surfs.length;i++){
            Vec3[] temp = surfToVec(surfs[i]);
            ret[i*3] = temp[0];
            ret[i*3+1] = temp[1];
            ret[i*3+2] = temp[2];
        }
        return ret;
    }

    public static SM64Surface[] block(int x, int y, int z, float blockTop, float blockBottom, short surface, short terrain)
    {
        var topQuad = generateQuadF(new Vector3f(0, blockTop, 0), new Vector3f(1, blockTop, 0), new Vector3f(1, blockTop, 1), new Vector3f(0, blockTop, 1), new Vector3f(z, y, x),surface,terrain);
        var bottomQuad = generateQuad(new Vector3f(0, blockBottom, 0), new Vector3f(1, blockBottom, 0), new Vector3f(1, blockBottom, 1), new Vector3f(0, blockBottom, 1), new Vector3f(z, y, x), surface, terrain);

        var sideAQuad = generateQuadF(new Vector3f(0, blockBottom, 0), new Vector3f(1, blockBottom, 0), new Vector3f(1, blockTop, 0), new Vector3f(0, blockTop, 0), new Vector3f(z, y, x), surface, terrain);
        var sideBQuad = generateQuadF(new Vector3f(0, blockBottom, 1), new Vector3f(0, blockBottom, 0), new Vector3f(0, blockTop, 0), new Vector3f(0, blockTop, 1), new Vector3f(z, y, x), surface, terrain);

        var sideCQuad = generateQuadF(new Vector3f(1, blockBottom, 1), new Vector3f(0, blockBottom, 1), new Vector3f(0, blockTop, 1), new Vector3f(1, blockTop, 1), new Vector3f(z, y, x), surface, terrain);
        var sideDQuad = generateQuadF(new Vector3f(1, blockBottom, 0), new Vector3f(1, blockBottom, 1), new Vector3f(1, blockTop, 1), new Vector3f(1, blockTop, 0), new Vector3f(z, y, x), surface, terrain);
        SM64Surface[] surfs = new SM64Surface[]
                {
                        // top
                        topQuad[0],
                        topQuad[1],

                        // bottom
                        bottomQuad[0],
                        bottomQuad[1],
                        // side A
                        sideAQuad[0],
                        sideAQuad[1],
                        // side B
                        sideBQuad[0],
                        sideBQuad[1],
                        // Side C
                        sideCQuad[0],
                        sideCQuad[1],
                        // Side D
                        sideDQuad[0],
                        sideDQuad[1],
                };

        return surfs;
    }

    public static SM64Surface[] plane(int x, int y, int z, float blockBottom, short surface, short terrain)
    {
        var bottomQuad = generateQuadF(new Vector3f(0, blockBottom, 0), new Vector3f(1, blockBottom, 0), new Vector3f(1, blockBottom, 1), new Vector3f(0, blockBottom, 1), new Vector3f(z, y, x), surface, terrain);


        return new SM64Surface[]
                {
                        // bottom
                        bottomQuad[0],
                        bottomQuad[1],
                };
    }
}
