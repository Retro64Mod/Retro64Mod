package com.dylanpdx.retro64;

import com.dylanpdx.retro64.config.Retro64Config;
import com.dylanpdx.retro64.events.clientControllerEvents;
import com.dylanpdx.retro64.sm64.SM64SurfaceType;
import com.dylanpdx.retro64.sm64.libsm64.*;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;

public class SM64EnvManager {

    public static boolean initialized = false;

    public static int playerModel=0;

    public static long attackDebounce=0;

    public static MChar selfMChar;

    public static ArrayList<SM64Surface> surfaces = new ArrayList<>();

    public static float lastVol=1;

    public static void updateSurfs(surfaceItem[] surfs){
        updateSurfs(surfs,null);
    }

    final static String ROM_HASH="9bef1128717f958171a4afac3ed78ee2bb4e86ce";

    /**
     * Update volume
     */
    public static void updateVol(){
        var vol = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER) * Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.PLAYERS);
        if (lastVol!=vol){
            lastVol=vol;
            Libsm64Library.INSTANCE.sm64_set_volume(vol);
        }
    }

    /**
     * Generates a solid floor at the given position; intended to prevent segfaults when there is no floor that exists
     * @param x the x coordinate of the floor
     * @param y the y coordinate of the floor
     * @param z the z coordinate of the floor
     * @return the generated floor surfaces
     */
    public static SM64Surface[] generateSafetyFloor(float x, float y, float z){
        return LibSM64SurfUtils.generateQuad(
                new Vector3f(-10, -0.5f, -10),
                new Vector3f(-10, -0.5f, 10),
                new Vector3f(10, -0.5f, 10),
                new Vector3f(10, -0.5f, -10),
                new Vector3f(x,y,z), SM64SurfaceType.SURFACE_DEFAULT.value, (short)0);
    }

    /**
     * Update the surfaces (send them to the libsm64 library)
     * @param surfaceItems the surfaces to update
     * @param extras extra surfaces to add on top of the given surfaces
     */
    public static void updateSurfs(surfaceItem[] surfaceItems, SM64Surface[] extras){
        surfaces.clear();
        if (extras!=null)
            Collections.addAll(surfaces, extras);
        if (selfMChar!=null)
            // generate floor below the player
            Collections.addAll(surfaces,generateSafetyFloor(selfMChar.state.position[0] / LibSM64.SCALE_FACTOR, -80, selfMChar.state.position[2] / LibSM64.SCALE_FACTOR));
        else
        {
            var playerPos = Minecraft.getInstance().player.position();
            Collections.addAll(surfaces,generateSafetyFloor((float)playerPos.x,-80,(float)playerPos.z));
        }
        if (RemoteMCharHandler.mChars!=null)
        for(MChar mChar : RemoteMCharHandler.mChars.values()){
            Collections.addAll(surfaces,generateSafetyFloor(mChar.state.position[0] / LibSM64.SCALE_FACTOR, -80, mChar.state.position[2] / LibSM64.SCALE_FACTOR));
        }
        if (surfaceItems!=null)
        {
            int surfCount = surfaceItems.length;
            for (int i = 0; i < surfCount; i++) {
                Vec3[] blockVertices = surfaceItems[i].verts; // vertices of the block
                if (blockVertices.length==1)
                {
                    if (surfaceItems[i].isCube())
                        Collections.addAll(surfaces,LibSM64SurfUtils.block((int)blockVertices[0].z,(int)blockVertices[0].y,(int)blockVertices[0].x,1f,0.1f,surfaceItems[i].material.value,surfaceItems[i].terrain));
                    else if (surfaceItems[i].isFlat())
                        Collections.addAll(surfaces,LibSM64SurfUtils.block((int)blockVertices[0].z,(int)blockVertices[0].y,(int)blockVertices[0].x,.03f,0.01f,surfaceItems[i].material.value,surfaceItems[i].terrain));
                }
                else
                    for (int j = 0; j < blockVertices.length; j+=4)
                    {
                        var one = new Vector3f(blockVertices[j]);
                        var two = new Vector3f(blockVertices[j+1]);
                        var three = new Vector3f(blockVertices[j+2]);
                        var four = new Vector3f(blockVertices[j+3]);
                        var quads = LibSM64SurfUtils.generateQuad(one,two, three, four,new Vector3f(0,0,0), surfaceItems[i].material.value, surfaceItems[i].terrain);
                        Collections.addAll(surfaces, quads);
                    }
            }
        }
        LibSM64.StaticSurfacesLoad(surfaces.toArray(new SM64Surface[0]));
    }

    public static void updateControls(Vec3 cam_fwd,Vec3 camPos,float joystickMult, boolean act_pressed,boolean jump_pressed, boolean crouch_pressed,
                                      boolean W_pressed, boolean A_pressed, boolean S_pressed, boolean D_pressed){
        selfMChar.inputs.buttonB= (byte) (act_pressed?1:0);
        selfMChar.inputs.buttonA= (byte) (jump_pressed?1:0);
        selfMChar.inputs.buttonZ= (byte) (crouch_pressed?1:0);
        selfMChar.inputs.stickX=0;
        selfMChar.inputs.stickY=0;
        Vec2 v2=null;
        if (Retro64.hasControllerSupport && (clientControllerEvents.input.x != 0 || clientControllerEvents.input.y != 0)){
            v2 = clientControllerEvents.input;
        }else{
            if (W_pressed) selfMChar.inputs.stickX += 1;
            if (A_pressed) selfMChar.inputs.stickY -= 1;
            if (S_pressed) selfMChar.inputs.stickX -= 1;
            if (D_pressed) selfMChar.inputs.stickY += 1;
            v2 = new Vec2(selfMChar.inputs.stickX, selfMChar.inputs.stickY).normalized();
        }
        selfMChar.inputs.stickX=v2.x*joystickMult;
        selfMChar.inputs.stickY=v2.y*joystickMult;
        selfMChar.inputs.camLookX= (float) cam_fwd.x;
        selfMChar.inputs.camLookZ= (float) cam_fwd.z;

        camPos= new Vec3(selfMChar.state.position[0],selfMChar.state.position[1],selfMChar.state.position[2]);//PUFixer.convertToSM64(camPos);
        // camera stuff
        selfMChar.inputs.cameraPosition[0] = (float) camPos.x;
        selfMChar.inputs.cameraPosition[1] = (float) camPos.y;
        selfMChar.inputs.cameraPosition[2] = (float) camPos.z;
    }

    /**
     * Get SHA1 hash of the given file
     * @param file the file to hash
     * @return the SHA1 hash of the file
     * @throws Exception if the file cannot be read
     */
    public static byte[] createSha1(File file) throws Exception  {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        fis.close();
        return digest.digest();
    }

    /**
     * Get SHA1 hash of the given file as a hex string
     * @param file the file to hash
     * @return the SHA1 hash of the file as a hex string
     * @throws Exception if the file cannot be read
     */
    public static String createSha1String(File file)  {
        byte[] sha1 = new byte[0];
        try {
            sha1 = createSha1(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : sha1) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * find a file that matches the ROM_HASH"
     * @return the file that matches the SHA1 hash, or null if no file matches
     */
    public static File getROMFile(){

        try{
            File configuredROMPath=new File(Retro64Config.ROM_PATH.get());
            if (configuredROMPath.exists() && createSha1String(configuredROMPath).equals(ROM_HASH)){
                return configuredROMPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        File[] files = new File(FMLPaths.MODSDIR.get().toString()).listFiles(f -> {
            try {
                return f.toPath().toString().endsWith("64") && createSha1String(f).equals(ROM_HASH);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        if (files == null || files.length == 0) {
            System.setProperty("java.awt.headless","false"); // This is probably a really bad idea, but MC's internal GUI system doesn't have an east way to create a file browser.
            // PR's are welcome if anyone wants to implement this using MC code.
            JDialog dialog = new JDialog();
            // show an error message
            var result = JOptionPane.showConfirmDialog(dialog,
                    new TranslatableComponent("menu.retro64.warnMissingROM").getString()+"\n"+
                    new TranslatableComponent("menu.retro64.warnPleaseSelectROM").getString(),
                                "Error", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                boolean valid = false;
                while (!valid){
                    // open file chooser
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Z64 ROM", "z64"));
                    fileChooser.setDialogTitle("Select a ROM");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setMultiSelectionEnabled(false);
                    int returnVal = fileChooser.showOpenDialog(dialog);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        if (createSha1String(file).equals(ROM_HASH)) {
                            Retro64Config.ROM_PATH.set(file.getAbsolutePath());
                            Retro64Config.ROM_PATH.save();
                            return file;
                        }else{
                            JOptionPane.showMessageDialog(dialog,
                            new TranslatableComponent("menu.retro64.warnInvalidROM").getString()+"\n"+
                    new TranslatableComponent("menu.retro64.warnPleaseSelectROM").getString(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }else if (returnVal == JFileChooser.CANCEL_OPTION){
                        valid = true;
                    }
                }
            }

            return null;//throw new FileNotFoundException("Could not find valid ROM");
        }
        Retro64Config.ROM_PATH.set(files[0].toPath().toString());
        Retro64Config.ROM_PATH.save();
        return files[0];
    }

    /**
     * Initialize the SM64 engine
     * @throws IOException if the ROM file cannot be read
     */
    public static void initLib() throws IOException {
        if (initialized)
            return;
        Retro64.LOGGER.info("Initializing Retro64. LibSM64 version: " + LibSM64.getVersion());
        var romFile = getROMFile();
        if (romFile == null) {
            return;
        }
        if (!romFile.getName().equals("baserom.us.z64")){
            var newPath=Path.of("mods","baserom.us.z64");
            Files.move(romFile.toPath(),newPath);
            romFile=newPath.toFile();
        }

        LibSM64.GlobalInit(romFile.getPath());

        initialized = true;
    }

    public static void sm64Update() {
        updateVol();
        selfMChar.fixedUpdate();
    }


}
