package com.smoothchunk.mixin;

import com.smoothchunk.SmoothchunkMod;
import com.smoothchunk.world.IChunkTimeSave;
import com.smoothchunk.world.PosTimeEntry;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayDeque;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin
{
    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap;

    @Shadow
    protected abstract boolean save(final ChunkAccess p_140259_);

    @Shadow
    protected abstract boolean saveChunkIfNeeded(final ChunkHolder p_198875_);

    @Shadow
    public abstract void broadcast(final Entity p_140202_, final Packet<?> p_140203_);

    private final Long2ObjectLinkedOpenHashMap<ChunkHolder> emptyMap = new Long2ObjectLinkedOpenHashMap<>();
    private final ArrayDeque<PosTimeEntry>                  toSave   = new ArrayDeque<>();

    @Redirect(method = "processUnloads", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectCollection;iterator()Lit/unimi/dsi/fastutil/objects/ObjectIterator;", remap = false))
    public ObjectIterator<ChunkHolder> smoothChunksaveChunks(final ObjectCollection instance)
    {
        final long currentGametime = level.getGameTime();

        if (currentGametime % 64 == 0)
        {
            for (final ChunkHolder entry : visibleChunkMap.values())
            {
                if (!entry.wasAccessibleSinceLastSave())
                {
                    continue;
                }

                final ChunkAccess chunkaccess = entry.getChunkToSave().getNow((ChunkAccess) null);
                if (!(chunkaccess instanceof ImposterProtoChunk) && !(chunkaccess instanceof LevelChunk))
                {
                    continue;
                }

                if (chunkaccess.isUnsaved())
                {
                    final long saveTimePoint = ((IChunkTimeSave) chunkaccess).getNextSaveTime();
                    if (saveTimePoint == 0)
                    {
                        ((IChunkTimeSave) chunkaccess).setSaveTimePoint(
                          currentGametime + SmoothchunkMod.config.getCommonConfig().chunkSaveDelay * 20 + SmoothchunkMod.rand.nextInt(20) * 20);
                        toSave.addLast(new PosTimeEntry(((IChunkTimeSave) chunkaccess).getNextSaveTime(), entry.getPos()));
                    }
                    else if (currentGametime > saveTimePoint)
                    {
                        ((IChunkTimeSave) chunkaccess).setSaveTimePoint(0);
                    }
                }
            }
        }

        int savedChunks = 0;
        for (int i = 0; i < 10; i++)
        {
            final PosTimeEntry posTimeEntry = toSave.peek();
            if (posTimeEntry == null)
            {
                break;
            }

            if (currentGametime > posTimeEntry.savetime)
            {
                final ChunkHolder holder = visibleChunkMap.get(posTimeEntry.pos.toLong());
                if (holder != null)
                {
                    if (saveChunkIfNeeded(holder))
                    {
                        savedChunks++;
                    }
                }
                toSave.pop();
            }
            else
            {
                break;
            }
        }

        if (savedChunks > 0 && SmoothchunkMod.config.getCommonConfig().debugLogging)
        {
            SmoothchunkMod.LOGGER.info("Smoothchunks saved chunks this tick: " + savedChunks);
        }

        return emptyMap.values().iterator();
    }
}
