package com.smoothchunk.config;

import com.google.gson.JsonObject;
import com.smoothchunk.SmoothchunkMod;

public class CommonConfiguration
{
    public int     chunkSaveDelay = 300;
    public boolean noSaveAll      = true;
    public boolean debugLogging   = false;

    protected CommonConfiguration()
    {
    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Delay before a chunk is saved to disk, default: 300 seconds. If you enable the noSaveAll config, it is suggest to set this to 180.");
        entry.addProperty("chunkSaveDelay", chunkSaveDelay);
        root.add("chunkSaveDelay", entry);

        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:", "Disables saving all chunks at once every 5 minutes(also does prevent the saveall command from saving chunks). "
                                      + "By default this does disable the save all chunks at once every 5 minutes because those are now saved smoothly distributed over time instead. default: true");
        entry2.addProperty("noSaveAll", noSaveAll);
        root.add("noSaveAll", entry2);

        final JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Enables debug logging of how many chunks got saved in a tick. default: false");
        entry3.addProperty("debugLogging", debugLogging);
        root.add("debugLogging", entry3);

        return root;
    }

    public void deserialize(JsonObject data)
    {
        if (data == null)
        {
            SmoothchunkMod.LOGGER.error("Config file was empty!");
            return;
        }

        try
        {
            chunkSaveDelay = data.get("chunkSaveDelay").getAsJsonObject().get("chunkSaveDelay").getAsInt();
            noSaveAll = data.get("noSaveAll").getAsJsonObject().get("noSaveAll").getAsBoolean();
            debugLogging = data.get("debugLogging").getAsJsonObject().get("debugLogging").getAsBoolean();
        }
        catch (Exception e)
        {
            SmoothchunkMod.LOGGER.error("Could not parse config file", e);
        }
    }
}
