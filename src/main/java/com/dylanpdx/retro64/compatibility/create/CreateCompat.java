package com.dylanpdx.retro64.compatibility.create;

import com.dylanpdx.retro64.FpsLimiter;
import com.dylanpdx.retro64.Utils;
import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import com.dylanpdx.retro64.sm64.libsm64.*;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateCompat {
    static HashMap<Contraption, Integer> contraptionSurfaces = new HashMap<>();
    static FpsLimiter fpsLimiter = new FpsLimiter(30);

    public static void updateContraptionSurfaces(LocalPlayer plr){
        for (Entity e: plr.level().getEntities(plr, plr.getBoundingBox().inflate(10))) {
            if (e instanceof AbstractContraptionEntity){
                var contraption = ((AbstractContraptionEntity) e).getContraption();
                if (!contraptionSurfaces.containsKey(contraption)){
                    var surfaces = getSurfacesForContraption(contraption);
                    var id = LibSM64.MovingSurfacesLoad(surfaces);
                    contraptionSurfaces.put(contraption, id);
                }
            }
        }
    }

    public static SM64SurfaceObject getSurfacesForContraption(Contraption c){
        var anchor = c.entity.position().toVector3f();
        anchor.add(.5f,.5f,.5f);
        if (c.entity.blocksBuilding || !c.simplifiedEntityColliders.isPresent()){
            return null;
        }
        SM64SurfaceObject obj = new SM64SurfaceObject();

        List<SM64Surface> surfaces = new ArrayList<>();
        for (AABB collider : c.simplifiedEntityColliders.get()){
            var verts = Utils.AABBToVec3List(collider);
            for (int i = 0; i < verts.size(); i+=4){
                var va = verts.get(i).toVector3f();
                var vb = verts.get(i+1).toVector3f();
                var vc = verts.get(i+2).toVector3f();
                var vd = verts.get(i+3).toVector3f();
                va.sub(.5f,.5f,.5f);
                vb.sub(.5f,.5f,.5f);
                vc.sub(.5f,.5f,.5f);
                var tris=LibSM64SurfUtils.generateQuad(va,vb,vc,vd,new Vector3f(0,0,0),(short) SM64SurfaceType.SURFACE_DEFAULT.value,(short)0);
                surfaces.add(tris[0]);
                surfaces.add(tris[1]);
            }
        }

        var allocArray = (SM64Surface[]) new SM64Surface().toArray(surfaces.size());
        for (int i = 0; i < surfaces.size(); i++){
            allocArray[i].force = surfaces.get(i).force;
            allocArray[i].terrain = surfaces.get(i).terrain;
            allocArray[i].type = surfaces.get(i).type;
            allocArray[i].vertices = surfaces.get(i).vertices;
            allocArray[i].write();
        }
        var cAnchor=PUFixer.convertToSM64(new Vector3f(anchor.x,anchor.y,anchor.z));
        allocArray[0].write();
        obj.surfaces = allocArray[0].getPointer();
        obj.surfaceCount = surfaces.size();
        obj.transform = new SM64ObjectTransform(new float[]{cAnchor.x,cAnchor.y,cAnchor.z}, new float[]{0,0,0});
        obj.write();

        return obj;
    }

    public static void removeContraption(Contraption c){
        if (contraptionSurfaces.containsKey(c)){
            var id = contraptionSurfaces.get(c);
            LibSM64.SurfaceObjectDelete(id);
            contraptionSurfaces.remove(c);
        }
    }

    public static void updateAllContraptions(){
        if (fpsLimiter.isLimited())
            return;
        for (var entry : contraptionSurfaces.entrySet()){
            var c = entry.getKey();
            var id = entry.getValue();
            var anchor = c.entity.position().toVector3f();
            anchor.add(.5f,.5f,.5f);
            var cAnchor=PUFixer.convertToSM64(new Vector3f(anchor.x,anchor.y,anchor.z));
            var oldTf=LibSM64.surfaceObjects.get(id).transform;

            var tf = new SM64ObjectTransform(new float[]{cAnchor.x,cAnchor.y,cAnchor.z}, new float[]{
                    c.entity.getRotationState().xRotation,
                    -c.entity.getRotationState().yRotation,
                    c.entity.getRotationState().zRotation});

            if (tf.position == oldTf.position && tf.eulerRotation == oldTf.eulerRotation){
                continue;
            }

            Libsm64Library.INSTANCE.sm64_surface_object_move(id, tf);
            LibSM64.surfaceObjects.get(id).transform = tf;
        }
    }

    public static void handleEntityLeave(EntityLeaveLevelEvent event){
        if (event.getEntity() instanceof AbstractContraptionEntity){
            var contraption = ((AbstractContraptionEntity) event.getEntity()).getContraption();
            removeContraption(contraption);
        }
    }

    public static void onupdateWorldGeometry(LocalPlayer plr){
        updateContraptionSurfaces(plr);
        updateAllContraptions();
    }
}
