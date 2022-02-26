package com.smoothchunk.world;

public interface IChunkTimeSave
{
    public long getNextSaveTime();

    void setSaveTimePoint(long timePoint);
}
