package com.smoothchunk.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig
{
    public int     chunkSaveDelay = 300;
    public boolean debugLogging   = false;

    public CommonConfiguration()
    {
    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Delay before a chunk is saved to disk, default: 300 seconds");
        entry.addProperty("chunkSaveDelay", chunkSaveDelay);
        root.add("chunkSaveDelay", entry);

        final JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Enables debug logging of how many chunks got saved in a tick. default: false");
        entry3.addProperty("debugLogging", debugLogging);
        root.add("debugLogging", entry3);

        return root;
    }

    public void deserialize(JsonObject data)
    {
        chunkSaveDelay = data.get("chunkSaveDelay").getAsJsonObject().get("chunkSaveDelay").getAsInt();
        debugLogging = data.get("debugLogging").getAsJsonObject().get("debugLogging").getAsBoolean();
    }
}
