package com.dylanpdx.retro64;

import com.dylanpdx.retro64.attachments.retro64attachments;
import com.dylanpdx.retro64.config.Retro64Config;
import com.dylanpdx.retro64.events.bothEvents;
import com.dylanpdx.retro64.events.bothEvents;
import com.dylanpdx.retro64.events.clientEvents;
import com.dylanpdx.retro64.events.serverEvents;
import com.dylanpdx.retro64.networking.SM64PacketHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.reflect.Method;
import java.util.logging.Logger;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod("retro64")
public class Retro64
{
    public static final String MOD_ID="retro64";
    public static boolean hasControllerSupport=false;
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    public static Method optifineGetShaderpackNameMethod;

    public Retro64(final IEventBus modBus, final ModContainer modContainer) {
        bothEvents bEvent=new bothEvents();
        // Register the setup method for modloading
        modBus.addListener(this::setup);
        //modBus.addListener(bEvent::registerCapabilities);
        RegistryHandler.init(modBus);
        retro64attachments.register(modBus);
        //SM64PacketHandler.registerPackets();

        modContainer.registerConfig(ModConfig.Type.CLIENT,Retro64Config.CONFIG_SPEC, "retro64.toml");

        if (FMLLoader.getDist() == Dist.CLIENT){
            clientEvents cEvent=new clientEvents();
            /*if (ModList.get().isLoaded("controllable")){
                clientControllerEvents cControllerEvent=new clientControllerEvents();
                NeoForge.EVENT_BUS.register(cControllerEvent);
                hasControllerSupport=true;
            }*/
            // optifine check, find "net.optifine.shaders" class through reflection
            try {
                var optiShaders = Class.forName("net.optifine.shaders.Shaders");
                LOGGER.info("Optifine detected");
                // get static function "getShaderPackName"
                optifineGetShaderpackNameMethod = optiShaders.getMethod("getShaderPackName");
            }catch (ClassNotFoundException | NoSuchMethodException e){
                LOGGER.info("Optifine not detected");
            }

            modBus.addListener(cEvent::registerKeybinds);
            modBus.addListener(cEvent::registerOverlays);
            NeoForge.EVENT_BUS.addListener(cEvent::gameTick);
            NeoForge.EVENT_BUS.addListener(cEvent::onScreenShown);
        }
        //serverEvents sEvent=new serverEvents();
        //modBus.register(sEvent);
        //modBus.register(bEvent);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        //Retro64GameRules.register();
    }


}
