package com.smoothchunk.world;

import net.minecraft.world.level.ChunkPos;

public class PosTimeEntry
{
    public final long     savetime;
    public final ChunkPos pos;

    public PosTimeEntry(final long savetime, final ChunkPos pos)
    {
        this.savetime = savetime;
        this.pos = pos;
    }
}
