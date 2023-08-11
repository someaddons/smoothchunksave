package com.smoothchunk;

import com.cupboard.config.CupboardConfig;
import com.smoothchunk.config.CommonConfiguration;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class SmoothchunkMod implements ModInitializer
{
    public static final String                              MODID  = "smoothchunk";
    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static       Random                              rand   = new Random();

    public SmoothchunkMod()
    {
    }

    @Override
    public void onInitialize()
    {
    }
}
