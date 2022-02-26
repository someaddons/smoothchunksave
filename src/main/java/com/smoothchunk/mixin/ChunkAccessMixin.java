package com.smoothchunk.mixin;

import com.smoothchunk.world.IChunkTimeSave;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkAccess.class)
public abstract class ChunkAccessMixin implements IChunkTimeSave
{
    long saveTimePoint = 0;

    @Override
    public long getNextSaveTime()
    {
        return saveTimePoint;
    }

    @Override
    public void setSaveTimePoint(final long timePoint)
    {
        saveTimePoint = timePoint;
    }
}
