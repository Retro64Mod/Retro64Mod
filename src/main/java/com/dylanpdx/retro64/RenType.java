package com.dylanpdx.retro64;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class RenType extends RenderType {

    public static final RenderType mcharRenderType = create("solid",
    DefaultVertexFormat.POSITION_COLOR_TEX,
    VertexFormat.Mode.TRIANGLES,
    256,
    false,
    true,
    RenderType.CompositeState.builder()
            .setLightmapState(LIGHTMAP)
            .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setTransparencyState(NO_TRANSPARENCY)
            .setOutputState(MAIN_TARGET)
            .setCullState(NO_CULL)
            //.setLightmapState(RenderStateShard.LIGHTMAP)
            .createCompositeState(false));

    public static final RenderType debugRenderType = create("lines", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 256,false,true,
    RenderType.CompositeState.builder().setShaderState(RENDERTYPE_LINES_SHADER).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(50)))
            .setLineState(new LineStateShard(OptionalDouble.empty())).setLayeringState(VIEW_OFFSET_Z_LAYERING)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(ITEM_ENTITY_TARGET).setWriteMaskState(COLOR_DEPTH_WRITE).setCullState(NO_CULL).createCompositeState(false));
    public RenType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    public static RenderType getMcharRenderType(){
        return mcharRenderType;
    }

    public static RenderType getDebugRenderType(){
        return debugRenderType;
    }
}
