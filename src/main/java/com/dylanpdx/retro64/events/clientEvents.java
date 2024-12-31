package com.dylanpdx.retro64.events;

import com.dylanpdx.retro64.*;
import com.dylanpdx.retro64.gui.CharSelectScreen;
import com.dylanpdx.retro64.gui.LibLoadWarnScreen;
import com.dylanpdx.retro64.gui.SMC64HeartOverlay;
import com.dylanpdx.retro64.maps.BlockMatMaps;
import com.dylanpdx.retro64.networking.Retro64Net;
import com.dylanpdx.retro64.networking.packets.McharPacket;
import com.dylanpdx.retro64.sm64.*;
import com.dylanpdx.retro64.sm64.libsm64.LibSM64;
import com.dylanpdx.retro64.sm64.libsm64.Libsm64Library;
import com.dylanpdx.retro64.sm64.libsm64.MChar;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles events for the client side
 */
public class clientEvents {
    static boolean clickDebounce=false;
    static boolean debug=false; // displays debug info
    static boolean initScreenDone =false;

    @SubscribeEvent
    public void registerOverlays(RegisterGuiLayersEvent event){
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath("retro64","sm64_heart_overlay"), new SMC64HeartOverlay());
    }

    @SubscribeEvent
    public void registerKeybinds(RegisterKeyMappingsEvent event){
        Keybinds.register(event);
    }

    @SubscribeEvent
    public void gameTick(ClientTickEvent.Pre event){
        //if (event.phase== TickEvent.Phase.END)
            //return;
        try{
            if (Minecraft.getInstance().player==null){
                // If the player is null, we're probably not in-game. There's probably a simpler way of checking for this
                if (SM64EnvManager.selfMChar!=null)
                {
                    LibSM64.MCharDelete(SM64EnvManager.selfMChar.id);
                    SM64EnvManager.selfMChar=null;
                }
            }else{
                /// Handle toggling modes
                if (Keybinds.getMToggle().isDown() && !clickDebounce){
                    if (Utils.isConnectedToVanillaServer())
                        return; // don't allow toggling in vanilla servers
                    var result = RemoteMCharHandler.toggleMChar(Minecraft.getInstance().player);
                    clickDebounce=true;
                    if (!result){
                        PacketDistributor.sendToServer(new McharPacket(
                                new Vec3(0,0,0).toVector3f(),
                                new byte[0],
                                new Vector3f(0,0,0),
                                -1, -1, // indicator that the player is not in R64 mode
                                Minecraft.getInstance().player.getGameProfile()
                        ));
                    }

                }else if (!Keybinds.getMToggle().isDown() && clickDebounce)
                    clickDebounce=false;

                /// Handle character select menu
                if (RemoteMCharHandler.getIsMChar(Minecraft.getInstance().player) && SM64EnvManager.selfMChar != null && SM64EnvManager.selfMChar.state != null){
                    if (Keybinds.getMMenu().isDown() && Minecraft.getInstance().level!=null)
                        Minecraft.getInstance().setScreen(new CharSelectScreen());
                    if (Minecraft.getInstance().player.isSpectator())
                        RemoteMCharHandler.mCharOff(Minecraft.getInstance().player);
                    if (Minecraft.getInstance().player.getAbilities().flying)
                        Minecraft.getInstance().player.getAbilities().flying = false;

                    int[] capFlags = {SM64MCharStateFlags.MCHAR_WING_CAP.getValue(),SM64MCharStateFlags.MCHAR_METAL_CAP.getValue(),SM64MCharStateFlags.MCHAR_VANISH_CAP.getValue()};
                    String[] capRegNames = {Utils.getRegistryName(RegistryHandler.WING_CAP_ITEM.get()),Utils.getRegistryName(RegistryHandler.METAL_CAP_ITEM.get()),Utils.getRegistryName(RegistryHandler.VANISH_CAP_ITEM.get())};

                    for (int i = 0;i<capFlags.length;i++){
                        var check=Utils.getRegistryName(Minecraft.getInstance().player.getInventory().armor.get(3).getItem()).equals(capRegNames[i]);
                        if (check && (SM64EnvManager.selfMChar.state.flags & capFlags[i]) != capFlags[i])
                            LibSM64.MCharChangeState(SM64EnvManager.selfMChar.id, SM64EnvManager.selfMChar.state.flags | capFlags[i]);
                        else if (!check && (SM64EnvManager.selfMChar.state.flags & capFlags[i]) == capFlags[i])
                            LibSM64.MCharChangeState(SM64EnvManager.selfMChar.id, SM64EnvManager.selfMChar.state.flags & ~capFlags[i]);
                    }
                    //var eState = (SM64MCharStateFlags.MCHAR_CAP_ON_HEAD.getValue() | SM64MCharStateFlags.MCHAR_WING_CAP.getValue());
                }
            }
        }catch (NullPointerException e){
            // when switching gamemodes, sometimes MChar becomes null.
            // I'm not sure why this happens, maybe related to native code?
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre rpe){

        if (!RemoteMCharHandler.getIsMChar(rpe.getEntity())) // don't render if not in R64 mode
            return;

        if (!rpe.getEntity().isLocalPlayer())
        {
            // Render remote players (multiplayer)
            RemoteMCharHandler.tickAll(); // Tick the animation of all remote players
            mCharRenderer.renderOtherPlayer(rpe);
            rpe.setCanceled(true);
            rpe.getRenderer().renderNameTag((AbstractClientPlayer) rpe.getEntity(), rpe.getEntity().getDisplayName(), rpe.getPoseStack(), rpe.getMultiBufferSource(), rpe.getPackedLight(),rpe.getPartialTick());
        }else{
            // Prevent player from being ticked if rendered in UI
            if (!(rpe.getPackedLight()== 15728880 && rpe.getPartialTick()==1.0F))
                mCharTick();
            else // Face the camera if in UI!
                rpe.getPoseStack().mulPose(Utils.quaternionFromXYZ(
                        (float)Math.toRadians(0),
                        (float)Math.toRadians(180)- SM64EnvManager.selfMChar.state.faceAngle,
                        (float)Math.toRadians(0)));

            mCharRenderer.renderMChar(rpe, SM64EnvManager.selfMChar);
            rpe.setCanceled(true);
        }
    }

    //@SubscribeEvent
    public void worldRender(RenderLevelStageEvent event){
        LocalPlayer plr = Minecraft.getInstance().player;
        if (plr.isAlive() && RemoteMCharHandler.getIsMChar(plr)){
            if (Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON){
                mCharTick();
            }
            if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT){
                Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
            }
        }
        // render debug
        if (isDebug()){
            var stack = event.getPoseStack();
            var rt = RenType.getDebugRenderType();
            var buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(rt);
            stack.pushPose();
            Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            stack.translate(-cam.x, -cam.y, -cam.z);
            PoseStack.Pose p = stack.last();
            for (var surf : SM64EnvManager.surfaces){
                float cR=0;
                float cG=0;
                float cB=0;
                if (surf.type == (short) SM64SurfaceType.SURFACE_BURNING.value) {
                    cR = 1;
                    cG = 0;
                    cB = 0;
                } else if (surf.type == (short) SM64SurfaceType.SURFACE_HANGABLE.value) {
                    cR = 1;
                    cG = 0;
                    cB = 1;
                } else if (surf.type == (short) SM64SurfaceType.SURFACE_ICE.value) {
                    cR = 0;
                    cG = 1;
                    cB = 1;
                } else if (surf.type == (short) SM64SurfaceType.SURFACE_SHALLOW_QUICKSAND.value) {
                    cR = .3f;
                    cG = .3f;
                    cB = .3f;
                } else {
                    cR = 1;
                    cG = 1;
                    cB = 1;
                }
                for (int i = 0; i < surf.vertices.length; i += 3)
                    buffer.addVertex(p.pose(),surf.vertices[i]/LibSM64.SCALE_FACTOR,(surf.vertices[i+1]/LibSM64.SCALE_FACTOR)+0.01f,surf.vertices[i+2]/LibSM64.SCALE_FACTOR).setColor(cR,cG,cB,1f);
            }
            stack.popPose();
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch(rt);
        }
    }

    @SubscribeEvent
    public void onRenderGameUI(RenderGuiLayerEvent event){
        /*if (RemoteMCharHandler.getIsMChar(Minecraft.getInstance().player) && (event.getOverlay() == GuiOverlayManager.findOverlay() || event.getOverlay() == ForgeIngameGui.FOOD_LEVEL_ELEMENT || event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT)){
            event.setCanceled(true);
        }*/
    }

    @SubscribeEvent
    public void onRenderGameUI(CustomizeGuiOverlayEvent.DebugText event){

        if (isDebug()){
            DrawDebugUI(event);
        }
    }

    private void DrawDebugUI(CustomizeGuiOverlayEvent.DebugText event) {
        var font = Minecraft.getInstance().font;
        var plr = Minecraft.getInstance().player;
        if (RemoteMCharHandler.getIsMChar(plr)){
            String debugText="";
            debugText+=String.format("currentAction: %s (%s)\n", SM64EnvManager.selfMChar.state.action, SM64MCharAction.getAllActions(SM64EnvManager.selfMChar.state.action));
            debugText+=String.format("currentActionFlags: %s\n", SM64MCharActionFlags.getAllFlags(SM64EnvManager.selfMChar.state.action));
            debugText+=String.format("stateFlags: %s (%s)\n", SM64EnvManager.selfMChar.state.flags, SM64MCharStateFlags.getAllFlags(SM64EnvManager.selfMChar.state.flags));
            debugText+=String.format("faceAngle: %s\n", SM64EnvManager.selfMChar.state.faceAngle);
            debugText+=String.format("vtx: %s\n", SM64EnvManager.selfMChar.getVertices().length);

            debugText+=String.format("health: %s\n", SM64EnvManager.selfMChar.state.health);
            var anim = SM64EnvManager.selfMChar.animInfo;
            debugText+=String.format("anim id: %s; accel: %s; frame: %s; angle: %s\n",anim.animID,anim.animAccel,anim.animFrame, SM64EnvManager.selfMChar.animYRot);
            debugText+=String.format("velocity: X %s; Y %s; Z %s\n", SM64EnvManager.selfMChar.state.velocity[0], SM64EnvManager.selfMChar.state.velocity[1], SM64EnvManager.selfMChar.state.velocity[2]);

            if (plr.isPassenger()){
                debugText+="MC passenger: "+plr.getVehicle().yRotO+"\n";
            }
            var dat=debugText.split("\n");
            for (int i = 0;i<dat.length;i++){
                //font.draw(event.getPoseStack(),dat[i],10,10+(10*i),0xffffffff);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()) // do not carry over capabilities on death
            return;
        RemoteMCharHandler.setIsMChar(event.getEntity(),RemoteMCharHandler.getIsMChar(event.getOriginal()));
    }


    @SubscribeEvent
    public void onPlayerJoinWorld(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event){
        if (event.getEntity() instanceof Player){
            Player plr = (Player) event.getEntity();
            if (plr.isLocalPlayer() && RemoteMCharHandler.wasMCharDimm!=null && RemoteMCharHandler.wasMCharDimm!=plr.level().dimension()){
                // Very lazy fix - Don't tick the player until the world finishes loading
                // TODO: timeout?
                Thread t = new Thread(()->{
                    while (Minecraft.getInstance().screen != null && Minecraft.getInstance().screen instanceof net.minecraft.client.gui.screens.ReceivingLevelScreen){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    RemoteMCharHandler.mCharOn(plr,false);
                });
                t.start();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveWorld(net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent event){
        if (event.getEntity() instanceof Player){
            Player plr = (Player) event.getEntity();
            if (RemoteMCharHandler.getIsMChar(plr) || RemoteMCharHandler.getState(plr) != null){
                RemoteMCharHandler.mCharOff(plr);
                System.out.println("Removed mario for player "+plr.getGameProfile().getName());
            }
        }
    }

    public static boolean isDebug(){
        return debug;
    }

    /**
     * SM64Lib tick
     */
    static void mCharTick(){
        if (SM64EnvManager.selfMChar == null || SM64EnvManager.selfMChar.id == -1)
            return;
        LocalPlayer plr = Minecraft.getInstance().player;
        ClientLevel world = Minecraft.getInstance().level;
        if (plr.isOnFire())
            plr.clearFire();
        var mchar = SM64EnvManager.selfMChar;

        if (world.isClientSide && Minecraft.getInstance().getSingleplayerServer()!=null && !Minecraft.getInstance().getSingleplayerServer().isPublished() && Minecraft.getInstance().isPaused())
            return;

        if (Utils.isConnectedToVanillaServer())
        {
            RemoteMCharHandler.mCharOff(plr);
            return;
        }
        world.getProfiler().push("retro64_mchar_tick");

        // potion effects handling
        float joystickMult=1;
        boolean poisoned=false;
        for (var effect : plr.getActiveEffects()){
            switch (Utils.getRegistryName(effect.getEffect().value())){
                case "minecraft:speed":
                    float extraMult = (effect.getAmplifier()+1)*0.1f;
                    joystickMult+=extraMult;
                    break;
                case "minecraft:slowness":
                    float slownessMult = (effect.getAmplifier()+1)*-0.15f;
                    joystickMult+=slownessMult;
                    if (joystickMult<0)
                        joystickMult=0;
                    break;
                case "minecraft:levitation":
                    mchar.setVelocity(new Vector3f(mchar.velocity().x(),
                            Math.min(mchar.velocity().y() + (0.04f * (effect.getAmplifier()+1)), 0.2f),
                            mchar.velocity().z()));
                    LibSM64.MCharChangeAction(mchar.id,SM64MCharAction.ACT_FREEFALL);
                    break;
                case "minecraft:slow_falling":
                    mchar.setVelocity(new Vector3f(mchar.velocity().x(),
                            Math.max(mchar.velocity().y(),-.2f),
                            mchar.velocity().z()));
                    break;
                case "minecraft:poison":
                    poisoned=true;
                    break;

            }
        }
        // update movement
        updatePlayerMovement(plr,joystickMult);

        /*if (Keybinds.getDebugToggle().consumeClick()){
            debug = !debug;
        }*/

        // sleep handling
        updatePlayerSleep(plr,mchar);

        updatePlayerRiding(plr);

        boolean optifineFix=false;
        if (Retro64.optifineGetShaderpackNameMethod!=null){
            try {
                var shaderPack = (String)Retro64.optifineGetShaderpackNameMethod.invoke(null);
                optifineFix = shaderPack!=null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        updateWorldGeometry(plr, world, optifineFix);

        Libsm64Library.INSTANCE.sm64_mChar_set_water_level(SM64EnvManager.selfMChar.id, (int) (Utils.findWaterLevel(world,plr.blockPosition())*LibSM64.SCALE_FACTOR)+90);
        Libsm64Library.INSTANCE.sm64_mChar_set_gas_level(SM64EnvManager.selfMChar.id, !poisoned?-10000:10000);

        updatePlayerAttack(plr, world);

        SM64EnvManager.selfMChar.state.currentModel= SM64EnvManager.playerModel;

        SM64EnvManager.sm64Update();

        if (!plr.isPassenger())
            updatePlayerPosition(plr);

        updatePlayerBreath(plr);

        updatePlayerDeath(plr);
        world.getProfiler().pop();
    }

    /**
     * Update player position, relay it to the server.
     * Teleports either mChar -> player or player -> mChar depending on distance
     * @param plr player
     */
    private static void updatePlayerPosition(LocalPlayer plr) {
        // Teleport the MChar to the player if it's too far away
        if (new Vec3(SM64EnvManager.selfMChar.x(), SM64EnvManager.selfMChar.y(), SM64EnvManager.selfMChar.z()).distanceTo(plr.position())>(2+ SM64EnvManager.selfMChar.velocityMagnitude()) && !plr.isSleeping()){
            SM64EnvManager.selfMChar.teleport(plr.position());
        }else{ // constantly update the real player's position
            plr.moveTo(SM64EnvManager.selfMChar.x(), SM64EnvManager.selfMChar.y(), SM64EnvManager.selfMChar.z());
            plr.setDeltaMovement(0,-0.01f,0);
        }
        // tell the server about the player's position. In the future this should be checked to prevent exploits
        try{
            PacketDistributor.sendToServer(new McharPacket(
                    new Vec3(SM64EnvManager.selfMChar.x(), SM64EnvManager.selfMChar.y(), SM64EnvManager.selfMChar.z()).toVector3f(),
                    SM64EnvManager.selfMChar.animInfo.serialize(),
                    new Vector3f(SM64EnvManager.selfMChar.animXRot, SM64EnvManager.selfMChar.animYRot, SM64EnvManager.selfMChar.animZRot), // animation rotations
                    SM64EnvManager.selfMChar.state.action, SM64EnvManager.selfMChar.state.currentModel,
                    plr.getGameProfile()
            ));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Check if mChar has died
     * @param plr the player
     */
    private static void updatePlayerDeath(LocalPlayer plr) {
        // update death
        if (SM64EnvManager.selfMChar.deathTime!=0 && Util.getEpochMillis()- SM64EnvManager.selfMChar.deathTime>5000){
            var act = SM64EnvManager.selfMChar.state.action;
            if ((SM64MCharActionFlags.ACT_FLAG_INTANGIBLE.value & act) != 0)
                RemoteMCharHandler.mCharOff(plr,true,true);
        }
    }

    /**
     * Update the player's air remaining. If in R64 mode, mChar's health is synced to player's breath
     * @param plr the player
     */
    private static void updatePlayerBreath(LocalPlayer plr) {
        if (plr.isUnderWater()){
            var hpPct = (SM64EnvManager.selfMChar.state.health-255)/2176f;
            plr.setAirSupply((int) (300*hpPct));
        }
    }

    /**
     * Update the player's attack status. Reads from libsm64 and relays it to MC
     * @param plr The player
     * @param world The world
     */
    private static void updatePlayerAttack(LocalPlayer plr, ClientLevel world) {
        if (SM64EnvManager.selfMChar!=null && selfAttacking(SM64EnvManager.selfMChar)){
            if(SM64EnvManager.attackDebounce < System.currentTimeMillis())
            {
                SM64EnvManager.attackDebounce = System.currentTimeMillis() + 500;
                for (Entity e: world.getEntities(null, AABB.ofSize(plr.position(),3,2,3))) {
                    if (e instanceof LivingEntity && e != plr && e.isAlive()){
                        Minecraft.getInstance().gameMode.attack(plr,e);
                    }
                }
            }
        }
    }

    /**
     * Updates the world geometry and relay it to libsm64 so that collisions can be calculated
     * @param plr the player
     * @param world the world
     * @param optifineFix is Optifine detected and shaders are enabled?
     */
    private static void updateWorldGeometry(LocalPlayer plr, ClientLevel world, boolean optifineFix) {
        BlockPos playerPos= plr.blockPosition();
        boolean vanish = (SM64EnvManager.selfMChar.state.flags & SM64MCharStateFlags.MCHAR_VANISH_CAP.getValue()) == SM64MCharStateFlags.MCHAR_VANISH_CAP.getValue();

        // get area around player
        Iterable<BlockPos> nearbyBlockPos = BlockPos.betweenClosed(playerPos.offset(-1,-2,-1),playerPos.offset(1,2,1));
        ArrayList<surfaceItem> surfaces = new ArrayList<>(); // surfaces to send to libsm64
        var worldBorder=world.getWorldBorder();
        for (BlockPos bp:nearbyBlockPos) {

            BlockPos nearbyBlock=bp.immutable();

            if (nearbyBlock.getX() > worldBorder.getMaxX() || nearbyBlock.getX() < worldBorder.getMinX() || nearbyBlock.getZ() > worldBorder.getMaxZ() || nearbyBlock.getZ() < worldBorder.getMinZ())
            {
                surfaces.add(new surfaceItem(new Vec3(nearbyBlock.getX(),nearbyBlock.getY(),nearbyBlock.getZ()),true,false,SM64SurfaceType.SURFACE_DEFAULT, SM64TerrainType.Stone));
                continue;
            }

            BlockState nearbyBlockState = world.getBlockState(nearbyBlock);

            List<Vec3> collisionVertices = new ArrayList<>();

            boolean passThrough = (vanish && nearbyBlockState.getTags().anyMatch(t -> t == BlockMatMaps.vanishable)) || nearbyBlockState.getTags().anyMatch(t -> t == BlockMatMaps.intangible);
            boolean useModel = nearbyBlockState.getTags().anyMatch(t -> t == BlockMatMaps.useModel);


            BakedModel blockModel = Minecraft.getInstance().getModelManager().getModel(BlockModelShaper.stateToModelLocation(nearbyBlockState));

            // Use the actual hitbox, except if the block is in the "use_model" tag
            List<Vec3> blockModelQuads = useModel ? Utils.getQuadsFromModel(blockModel,nearbyBlockState,Minecraft.getInstance().level.random, passThrough) : Utils.getQuadsFromHitbox(world, bp, passThrough);

            for (Vec3 quad :blockModelQuads) {
                collisionVertices.add(quad.add(nearbyBlock.getX(),nearbyBlock.getY(),nearbyBlock.getZ()));
            }

            if (!passThrough){

                    if (optifineFix && useModel){
                        // If optifine is installed + shaders enabled, replace all model collisions with squares
                        collisionVertices.clear();
                        surfaces.add(new surfaceItem(new Vec3(nearbyBlock.getX(),nearbyBlock.getY(),nearbyBlock.getZ()),true,false, BlockMatMaps.getFromTag(nearbyBlockState), SM64TerrainType.Stone));
                    }

                    if (BlockMatMaps.replaceCollisionMat(nearbyBlockState)){
                        // force cube collision box
                        collisionVertices.clear();
                        surfaces.add(new surfaceItem(new Vec3(nearbyBlock.getX(),nearbyBlock.getY(),nearbyBlock.getZ()),true,false,BlockMatMaps.getFromTag(nearbyBlockState), SM64TerrainType.Stone));
                    }else if (BlockMatMaps.flatCollisionMat(nearbyBlockState)){
                        // force flat collision box
                        collisionVertices.clear();
                        surfaces.add(new surfaceItem(new Vec3(nearbyBlock.getX(),nearbyBlock.getY(),nearbyBlock.getZ()),false,true,BlockMatMaps.getFromTag(nearbyBlockState), SM64TerrainType.Stone));
                    }else if (nearbyBlockState.getBlock().getFriction()>=0.7f){
                        // if block is slippery, set it to ice
                        surfaces.add(new surfaceItem(collisionVertices,SM64SurfaceType.SURFACE_ICE, SM64TerrainType.Snow));
                    }else if (nearbyBlockState.getBlock().getSpeedFactor()<0.6f) {
                        // if block is slow (soul sand?), set it to quicksand
                        surfaces.add(new surfaceItem(collisionVertices,SM64SurfaceType.SURFACE_SHALLOW_QUICKSAND, SM64TerrainType.Sand));
                    }else
                    {
                        var stype = nearbyBlockState.getBlock().getSoundType(nearbyBlockState, world,nearbyBlock, plr);
                        var stSound = Utils.getRegistryName(stype.getStepSound());
                        short type=SM64TerrainType.Stone;
                        // Determine what material a block is by its sound type
                        switch (stSound){
                            case "minecraft:block.gravel.step":
                            case "minecraft:block.sand.step":
                                type=SM64TerrainType.Sand;
                                break;
                            case "minecraft:block.grass.step":
                                type=SM64TerrainType.Grass;
                                break;
                            case "minecraft:block.wood.step":
                                type=SM64TerrainType.Spooky;
                                break;
                            default:
                                break;
                        }
                        surfaces.add(new surfaceItem(collisionVertices,BlockMatMaps.getFromTag(nearbyBlockState), type));
                    }
            }
        }

        // convert to array and pass to libsm64
        surfaceItem[] surfItems = new surfaceItem[surfaces.size()];
        surfaces.toArray(surfItems);
        SM64EnvManager.updateSurfs(surfItems);
    }

    /**
     * Check if the player is riding an entity (i.e pig, horse, etc) and set the player's animation accordingly
     * @param plr The player to check
     */
    private static void updatePlayerRiding(LocalPlayer plr) {
        // check if player is riding a mob, if so set mChar to ACT_GRABBED
        if (plr.isPassenger()){
            var pos = plr.getVehicle().position();
            SM64EnvManager.selfMChar.teleport(pos);
            var ang = (float)(Math.toRadians(plr.getVehicle().getYRot()/-9.5f));//(short)(plr.getVehicle().yRotO*(32768*Math.PI));
            Libsm64Library.INSTANCE.sm64_mChar_set_angle(SM64EnvManager.selfMChar.id, ang);
            if (SM64EnvManager.selfMChar.state.action!= SM64MCharAction.ACT_GRABBED.id)
                LibSM64.MCharChangeAction(SM64EnvManager.selfMChar.id,SM64MCharAction.ACT_GRABBED.id);
        }else if (!plr.isPassenger() && SM64EnvManager.selfMChar.state.action==SM64MCharAction.ACT_GRABBED.id){
            LibSM64.MCharChangeAction(SM64EnvManager.selfMChar.id,SM64MCharAction.ACT_IDLE.id);
        }
    }

    /**
     * Updates the player's controls, pass that to libsm64
     * @param plr the player
     * @param joystickMult the joystick multiplier, used for potion effects
     */
    private static void updatePlayerMovement(LocalPlayer plr,float joystickMult) {
        var cam_fwd = plr.getLookAngle().yRot((float)Math.toRadians(90));//Objects.requireNonNull(Minecraft.getInstance().getCameraEntity()).getForward();
        var cam_pos=Minecraft.getInstance().getCameraEntity().position();
        SM64EnvManager.updateControls(cam_fwd,cam_pos, joystickMult
        ,Keybinds.getActKey().isDown(),plr.input.jumping,plr.input.shiftKeyDown,plr.input.up,plr.input.left,plr.input.down,plr.input.right);
    }

    /**
     * Check if the player is sleeping in MC and relay that to libsm64
     * @param plr the player to check
     * @param mchar the mchar to update
     */
    private static void updatePlayerSleep(LocalPlayer plr,MChar mchar) {
        if (plr.isSleeping() && mchar.state.action!=SM64MCharAction.ACT_SLEEPING.id && mchar.state.action!=SM64MCharAction.ACT_START_SLEEPING.id){
            if (plr.getSleepingPos().isPresent() && plr.getSleepTimer()>1){
                var bedPos = plr.getSleepingPos().get().immutable();
                var bedPos2 = bedPos.offset(plr.getBedOrientation().getOpposite().getNormal());
                // get midpoint of bedPos and bedPos2
                var midpoint = new Vec3(
                        (bedPos.getX()+bedPos2.getX())/2f,(bedPos.getY()+bedPos2.getY())/2f,(bedPos.getZ()+bedPos2.getZ())/2f);
                midpoint = midpoint.add(.5f,.5f,.5f);
                var sleepDir= plr.getBedOrientation().toYRot()+75;
                var ang = (float)(Math.toRadians(sleepDir/9.5f));
                Libsm64Library.INSTANCE.sm64_mChar_set_angle(mchar.id, ang);
                SM64EnvManager.selfMChar.teleport(midpoint);

                LibSM64.MCharChangeAction(SM64EnvManager.selfMChar.id, SM64MCharAction.ACT_SLEEPING);
                Libsm64Library.INSTANCE.sm64_mChar_set_action_state(mchar.id,(short)1);
            }
        }
    }

    /**
     * Check if the player is attacking
     * @param mchar the player's mChar
     * @return true if the player is attacking
     */
    public static boolean selfAttacking(MChar mchar){
        if (mchar.state.testActionFlag(SM64MCharActionFlags.ACT_FLAG_ATTACKING)){
            return mchar.state.action == SM64MCharAction.ACT_PUNCHING.id ||
                    mchar.state.action == SM64MCharAction.ACT_MOVE_PUNCHING.id ||
                    mchar.state.action == SM64MCharAction.ACT_JUMP_KICK.id ||
                    mchar.state.action == SM64MCharAction.ACT_GROUND_POUND_LAND.id ||
                    mchar.state.action == SM64MCharAction.ACT_SLIDE_KICK.id ||
                    mchar.state.action == SM64MCharAction.ACT_SLIDE_KICK_SLIDE.id ||
                    mchar.state.action == SM64MCharAction.ACT_DIVE.id ||
                    mchar.state.action == SM64MCharAction.ACT_DIVE_SLIDE.id;

        }else{
            return false;
        }

    }

    @SubscribeEvent
    public void onScreenShown(ScreenEvent.Opening event){
        if (initScreenDone)
            return;
        var rom = SM64EnvManager.getROMFile();
        if (event.getScreen() instanceof TitleScreen){
            if (!LibSM64.libFileExists() || !LibSM64.isSupportedVersion() || rom==null){
                MutableComponent reason;
                if (!LibSM64.libFileExists())
                    reason = Component.translatable("menu.retro64.warnNoDLL");
                else if (!LibSM64.isSupportedVersion())
                    reason = Component.translatable("menu.retro64.warnWrongVersion");
                else// if (!rom.exists())
                    reason = Component.translatable("menu.retro64.warnMissingROM");
                event.setNewScreen(new LibLoadWarnScreen(reason));
            }else{
                try {
                    SM64EnvManager.initLib();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        initScreenDone = true;
    }
}
