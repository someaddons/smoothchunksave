package com.smoothchunk.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration
{
    public final ForgeConfigSpec                      ForgeConfigSpecBuilder;
    public final ForgeConfigSpec.ConfigValue<Integer> chunkSaveDelay;
    public final ForgeConfigSpec.ConfigValue<Boolean> noSaveAll;
    public final ForgeConfigSpec.ConfigValue<Boolean> debugLogging;

    protected CommonConfiguration(final ForgeConfigSpec.Builder builder)
    {
        builder.push("Config category");

        builder.comment("Delay before a chunk is saved to disk, default: 300 seconds. If you enable the noSaveAll config, it is suggest to set this to 180.");
        chunkSaveDelay = builder.defineInRange("chunkSaveDelay", 300, 100, 3000);

        builder.comment("Disables saving all chunks at once every 5 minutes(also does prevent the saveall command from saving chunks), default: true");
        noSaveAll = builder.define("noSaveAll", true);

        builder.comment("Enables debug logging of how many chunks got saved in a tick. default: false");
        debugLogging = builder.define("debugLogging", false);

        // Escapes the current category level
        builder.pop();
        ForgeConfigSpecBuilder = builder.build();
    }
}
