package com.dylanpdx.retro64;

import com.dylanpdx.retro64.config.Retro64Config;
import com.dylanpdx.retro64.events.bothEvents;
import com.dylanpdx.retro64.events.clientControllerEvents;
import com.dylanpdx.retro64.events.clientEvents;
import com.dylanpdx.retro64.events.serverEvents;
import com.dylanpdx.retro64.networking.SM64PacketHandler;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.lang.reflect.Method;
import java.util.logging.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("retro64")
public class Retro64
{
    private static final RenderType CUTOUT = RenderType.cutout();
    public static final String MOD_ID="retro64";
    public static boolean hasControllerSupport=false;
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    public static Method optifineGetShaderpackNameMethod;

    public Retro64() {
        bothEvents bEvent=new bothEvents();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(bEvent::registerCapabilities);
        RegistryHandler.init();
        SM64PacketHandler.registerPackets();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,Retro64Config.CONFIG_SPEC, "retro64.toml");

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            Keybinds.register();
            clientEvents cEvent=new clientEvents();
            if (ModList.get().isLoaded("controllable")){
                clientControllerEvents cControllerEvent=new clientControllerEvents();
                MinecraftForge.EVENT_BUS.register(cControllerEvent);
                hasControllerSupport=true;
            }
            // optifine check, find "net.optifine.shaders" class through reflection
            try {
                var optiShaders = Class.forName("net.optifine.shaders.Shaders");
                LOGGER.info("Optifine detected");
                // get static function "getShaderPackName"
                optifineGetShaderpackNameMethod = optiShaders.getMethod("getShaderPackName");
            }catch (ClassNotFoundException | NoSuchMethodException e){
                LOGGER.info("Optifine not detected");
            }
            MinecraftForge.EVENT_BUS.register(cEvent);

        });
        serverEvents sEvent=new serverEvents();
        MinecraftForge.EVENT_BUS.register(sEvent);
        MinecraftForge.EVENT_BUS.register(bEvent);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(RegistryHandler.CASTLE_STAIRS.get(), CUTOUT);

    }


}
