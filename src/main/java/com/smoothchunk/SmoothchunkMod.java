package com.smoothchunk;

import com.cupboard.config.CupboardConfig;
import com.smoothchunk.config.CommonConfiguration;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.smoothchunk.SmoothchunkMod.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class SmoothchunkMod
{
    public static final String                              MODID  = "smoothchunk";
    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static       Random                              rand   = new Random();

    public SmoothchunkMod()
    {
    }
}
