package com.smoothchunk.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig
{
    public int     chunkSaveDelay = 300;
    public int chunkUnloadLimit = 20;
    public boolean debugLogging   = false;
    public boolean disableProtoSave = true;

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

        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:", "Maximum amount of chunks unloaded per tick, default: 20, vanilla:200");
        entry2.addProperty("chunkUnloadLimit", chunkUnloadLimit);
        root.add("chunkUnloadLimit", entry2);

        final JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Disables saving of protochunks(not fully generated chunks) to reduce saving lag. Default: true");
        entry4.addProperty("disableProtoSave", disableProtoSave);
        root.add("disableProtoSave", entry4);

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
        chunkUnloadLimit = data.get("chunkUnloadLimit").getAsJsonObject().get("chunkUnloadLimit").getAsInt();
        disableProtoSave = data.get("disableProtoSave").getAsJsonObject().get("disableProtoSave").getAsBoolean();
    }
}
