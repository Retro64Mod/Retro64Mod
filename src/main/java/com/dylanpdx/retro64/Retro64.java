package com.dylanpdx.retro64;

import com.dylanpdx.retro64.config.Retro64Config;
import com.dylanpdx.retro64.events.bothEvents;
import com.dylanpdx.retro64.events.clientEvents;
import com.dylanpdx.retro64.events.serverEvents;
import com.dylanpdx.retro64.networking.SM64PacketHandler;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

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

    public Retro64(FMLJavaModLoadingContext context) {
        var modBus = context.getModEventBus();
        var modContainer = context.getContainer();
        SM64PacketHandler.registerPackets();
        bothEvents bEvent=new bothEvents();
        // Register the setup method for modloading
        modBus.addListener(this::setup);
        RegistryHandler.init(modBus);

        modContainer.addConfig(new ModConfig(ModConfig.Type.CLIENT,Retro64Config.CONFIG_SPEC, modContainer));

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
            MinecraftForge.EVENT_BUS.addListener(cEvent::gameTick);
            MinecraftForge.EVENT_BUS.addListener(cEvent::onScreenShown);
            MinecraftForge.EVENT_BUS.addListener(cEvent::onPlayerRender);
            MinecraftForge.EVENT_BUS.addListener(cEvent::worldRender);
            MinecraftForge.EVENT_BUS.addListener(cEvent::onPlayerClone);
            MinecraftForge.EVENT_BUS.addListener(cEvent::onEntityJoinWorld);
            MinecraftForge.EVENT_BUS.addListener(cEvent::onEntityLeaveWorld);
        }

        // server
        MinecraftForge.EVENT_BUS.addListener(serverEvents::onPlayerJoin);
        MinecraftForge.EVENT_BUS.addListener(serverEvents::onPlayerPickupXP);
        MinecraftForge.EVENT_BUS.addListener(serverEvents::playerAttack);

        // both
        modBus.addListener(bEvent::registerCapabilities);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class,bEvent::attachCapabilitiesEntity);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        //Retro64GameRules.register();
    }


}
