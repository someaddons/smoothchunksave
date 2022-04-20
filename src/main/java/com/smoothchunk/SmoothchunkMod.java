package com.smoothchunk;

import com.smoothchunk.config.Configuration;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file

public class SmoothchunkMod implements ModInitializer
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

    @Override
    public void onInitialize()
    {
        config.load();
    }
}
