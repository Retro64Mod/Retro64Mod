package com.dylanpdx.retro64;

import com.dylanpdx.retro64.sm64.libsm64.MChar;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
// import com.mojang.math.Matrix4f;
// import com.mojang.math.Quaternion;
// import com.mojang.math.Transformation;
// import com.mojang.math.Vector3f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
// import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class mCharRenderer {

    /**
     * Render other player's character
     * @param rpe The render player event
     */
    public static void renderOtherPlayer(RenderPlayerEvent rpe){
        Player otherPlr = rpe.getPlayer();
        if (RemoteMCharHandler.mChars.containsKey(otherPlr)){
            // render mChar for other player
            MChar otherMChar = RemoteMCharHandler.mChars.get(otherPlr);
            renderMChar(rpe,otherMChar);
        }
        rpe.setCanceled(true);
    }

    /**
     * Renders the character of the player
     * If the player is crouching, it un-does the offset, referencing {@link net.minecraft.client.renderer.entity.player.PlayerRenderer#getRenderOffset(AbstractClientPlayer, float)}
     */
    public static void renderMChar(RenderPlayerEvent rpe,MChar mChar){
        if (mChar == null || mChar.id == -1)
            return;
        PoseStack st = rpe.getPoseStack();
        MultiBufferSource buff = rpe.getMultiBufferSource();
        VertexConsumer vc = buff.getBuffer(RenType.getMcharRenderType(mChar.state.currentModel==ModelData.VIBRI.getIndex())); // lazy fix for vibri model, enable culling
        st.pushPose();
        PoseStack.Pose p = st.last();
        Player plr = rpe.getPlayer();
        if (!plr.isLocalPlayer()){
            st.translate(-plr.getX(),-plr.getY(),-plr.getZ());
        }else{
            st.translate(-mChar.x(),-mChar.y(),-mChar.z());
        }

        if (plr.isCrouching())
            st.translate(0,0.125D,0);
        if (plr.isSleeping())
            st.translate(0,0.5D,0);

        if (rpe.getPlayer().isPassenger()){
            st.translate(0,.8f,0);
        }
        if (mChar.getVertices()!=null)
        {
            //Minecraft.getInstance().textureManager.register()
            //DynamicTexture t;
            RenderSystem.setShaderTexture(0,textureManager.getTextureForModel(mChar.state.currentModel,plr).getId());
            //RenderSystem.setShaderLights(new Vector3f(0,1,0),new Vector3f(0,1,0));
            int j =0;
            for (int i = 0;i< mChar.getVertices().length-9;i+=9) {
                drawFc(vc, p,
                        new float[]{mChar.getVertices()[i], mChar.getVertices()[i + 1], mChar.getVertices()[i + 2]},
                        new float[]{mChar.getVertices()[i + 3], mChar.getVertices()[i + 4], mChar.getVertices()[i + 5]},
                        new float[]{mChar.getVertices()[i + 6], mChar.getVertices()[i + 7], mChar.getVertices()[i + 8]},
                        // colors
                        new float[]{mChar.getColors()[i], mChar.getColors()[i + 1], mChar.getColors()[i + 2]},
                        new float[]{mChar.getColors()[i + 3], mChar.getColors()[i + 4], mChar.getColors()[i + 5]},
                        new float[]{mChar.getColors()[i + 6], mChar.getColors()[i + 7], mChar.getColors()[i + 8]},
                        // UV
                        new float[]{mChar.getUVs()[j], mChar.getUVs()[j + 1]},
                        new float[]{mChar.getUVs()[j+2], mChar.getUVs()[j + 3]},
                        new float[]{mChar.getUVs()[j+4], mChar.getUVs()[j + 5]}
                        ,Math.max(plr.getBrightness(),.1f),textureManager.getTextureWidth(mChar.state.currentModel),textureManager.getTextureHeight(mChar.state.currentModel)
                );
                j+=6;
            }
        }
        st.popPose();
    }

    /**
     * Draws a face with the given vertices
     * @param vc The vertex consumer
     * @param p The pose stack
     * @param _1 The first vertex
     * @param _2 The second vertex
     * @param _3 The third vertex
     * @param c_1 The first color
     * @param c_2 The second color
     * @param c_3 The third color
     * @param uv_1 The first UV
     * @param uv_2 The second UV
     * @param uv_3 The third UV
     * @param brightness The brightness of the color (lazily multiplied by the color)
     * @param tWidth The texture width
     * @param tHeight The texture height
     */
    public static void drawFc(VertexConsumer vc,PoseStack.Pose p,float[]_1,float[]_2,float[]_3,float[]c_1,float[]c_2,float[]c_3,float[] uv_1,float[] uv_2,float[] uv_3,float brightness,float tWidth,float tHeight){
        if (uv_1[0] != 1 && uv_1[1] != 1 && uv_2[0] != 1 && uv_2[1] != 1 && uv_3[0] != 1 && uv_3[1] != 1) {
            c_1 = new float[]{1, 1, 1};
            c_2 = new float[]{1, 1, 1};
            c_3 = new float[]{1, 1, 1};
        }else{
            uv_1=new float[]{140f/704f,7f/64f};
            uv_2=new float[]{140f/704f,7f/64f};
            uv_3=new float[]{140f/704f,7f/64f};
        }
        float uOffset = (.5f/tWidth);
        float vOffset = (.5f/tHeight);

        uv_1[0]+=uOffset;
        uv_1[1]+=vOffset;
        uv_2[0]+=uOffset;
        uv_2[1]+=vOffset;
        uv_3[0]+=uOffset;
        uv_3[1]+=vOffset;


        vc.vertex(p.pose(),_1[0],_1[1],_1[2]).color(c_1[0]*brightness,c_1[1]*brightness,c_1[2]*brightness,1).uv(uv_1[0],uv_1[1]).endVertex();
        vc.vertex(p.pose(),_2[0],_2[1],_2[2]).color(c_2[0]*brightness,c_2[1]*brightness,c_2[2]*brightness,1).uv(uv_2[0],uv_2[1]).endVertex();
        vc.vertex(p.pose(),_3[0],_3[1],_3[2]).color(c_3[0]*brightness,c_3[1]*brightness,c_3[2]*brightness,1).uv(uv_3[0],uv_3[1]).endVertex();
    }
}
