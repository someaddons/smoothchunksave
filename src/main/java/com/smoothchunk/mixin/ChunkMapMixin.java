package com.smoothchunk.mixin;

import com.smoothchunk.SmoothchunkMod;
import com.smoothchunk.world.IChunkTimeSave;
import com.smoothchunk.world.PosTimeEntry;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.function.BooleanSupplier;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin
{
    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap;

    @Shadow
    protected abstract boolean saveChunkIfNeeded(final ChunkHolder chunk, final long now);

    @Unique
    private final ArrayDeque<PosTimeEntry> toSave = new ArrayDeque<>();

    @Inject(method = "saveChunksEagerly", at = @At(value = "HEAD"), remap = false, cancellable = true)
    public void smoothChunksaveChunks(final BooleanSupplier haveTime, final CallbackInfo ci)
    {
        long now = Util.getMillis();
        ci.cancel();
        final long currentGametime = level.getGameTime();

        if (currentGametime % 64 == 0)
        {
            for (final ChunkHolder entry : visibleChunkMap.values())
            {
                if (!entry.wasAccessibleSinceLastSave() || !entry.isReadyForSaving())
                {
                    continue;
                }

                final ChunkAccess chunkaccess = entry.getLatestChunk();
                if (!(chunkaccess instanceof ImposterProtoChunk) && !(chunkaccess instanceof LevelChunk))
                {
                    continue;
                }

                if (chunkaccess.isUnsaved())
                {
                    final long saveTimePoint = ((IChunkTimeSave) chunkaccess).smoothchunk$getNextSaveTime();
                    if (saveTimePoint == 0)
                    {
                        ((IChunkTimeSave) chunkaccess).smoothchunk$setSaveTimePoint(
                            currentGametime + SmoothchunkMod.config.getCommonConfig().chunkSaveDelay * 20L + SmoothchunkMod.rand.nextInt(20) * 20);
                        toSave.addLast(new PosTimeEntry(((IChunkTimeSave) chunkaccess).smoothchunk$getNextSaveTime(), entry.getPos()));
                    }
                    else if (currentGametime > saveTimePoint)
                    {
                        ((IChunkTimeSave) chunkaccess).smoothchunk$setSaveTimePoint(0);
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
                final ChunkHolder holder = visibleChunkMap.get(posTimeEntry.pos.pack());
                if (holder != null)
                {
                    if (saveChunkIfNeeded(holder, now))
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
    }

    @Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;isExistingChunkFull(Lnet/minecraft/world/level/ChunkPos;)Z"), cancellable = true)
    private void checkExisting(final ChunkAccess chunkAccess, final CallbackInfoReturnable<Boolean> cir)
    {
        if (SmoothchunkMod.config.getCommonConfig().disableProtoSave && chunkAccess.getPersistedStatus().getChunkType() != ChunkType.LEVELCHUNK)
        {
            cir.setReturnValue(false);
        }
    }
}
