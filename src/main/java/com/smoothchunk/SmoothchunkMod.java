package com.smoothchunk;

import com.smoothchunk.config.Configuration;
import com.smoothchunk.event.ClientEventHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.smoothchunk.SmoothchunkMod.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class SmoothchunkMod
{
    public static final String        MODID  = "smoothchunk";
    public static final Logger        LOGGER = LogManager.getLogger();
    public static       Configuration config = new Configuration();
    public static       Random        rand   = new Random();

    public SmoothchunkMod()
    {
        //  ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (c, b) -> true));
        //  ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config.getCommonConfig().ForgeConfigSpecBuilder);
        //   Mod.EventBusSubscriber.Bus.MOD.bus().get().register(ModEventHandler.class);
        //  Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event)
    {
        // Side safe client event handler
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(ClientEventHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info(MODID + " mod initialized");
    }
}
