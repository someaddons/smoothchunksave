package com.smoothchunk.mixin;

import com.mojang.serialization.Codec;
import com.smoothchunk.SmoothchunkMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
import net.minecraft.world.level.chunk.storage.SectionStorage;
import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(PoiManager.class)
public abstract class PoiRepairMixin extends SectionStorage<PoiSection>
{
    public PoiRepairMixin(
        final SimpleRegionStorage simpleRegionStorage,
        final Function<Runnable, Codec<PoiSection>> function,
        final Function<Runnable, PoiSection> function2,
        final RegistryAccess registryAccess,
        final ChunkIOErrorReporter chunkIOErrorReporter,
        final LevelHeightAccessor levelHeightAccessor)
    {
        super(simpleRegionStorage, function, function2, registryAccess, chunkIOErrorReporter, levelHeightAccessor);
    }

    @Inject(method = "checkConsistencyWithBlocks", at = @At("RETURN"))
    private void checkPoiValidity(final SectionPos sectionPos, final LevelChunkSection blockSection, final CallbackInfo ci)
    {
        if (!SmoothchunkMod.config.getCommonConfig().detectBrokenPOIs)
        {
            return;
        }

        final Optional<PoiSection> section = this.getOrLoad(sectionPos.asLong());
        if (!section.isPresent())
        {
            return;
        }

        final PoiSection poiSection = section.get();
        if (!poiSection.records.isEmpty())
        {
            List<BlockPos> toRemove = new ArrayList<>();
            for (var entry : poiSection.records.short2ObjectEntrySet())
            {
                final short sectionPosKey = entry.getShortKey();
                final BlockState state = blockSection.getBlockState(SectionPos.sectionRelativeX(sectionPosKey),
                    SectionPos.sectionRelativeY(sectionPosKey),
                    SectionPos.sectionRelativeZ(sectionPosKey));
                final PoiRecord record = entry.getValue();
                if (!record.getPoiType().value().is(state))
                {
                    toRemove.add(record.getPos());
                    if (SmoothchunkMod.config.getCommonConfig().logPOIRepair)
                    {
                        SmoothchunkMod.LOGGER.info("Fixed broken POI:{} at:{} for:{}", record.getPoiType(), record.getPos().toShortString(), state);
                    }
                }
            }

            if (toRemove.isEmpty())
            {
                return;
            }

            for (final BlockPos pos : toRemove)
            {
                poiSection.remove(pos);
                final BlockState state = blockSection.getBlockState(SectionPos.sectionRelative(pos.getX()),
                    SectionPos.sectionRelative(pos.getY()),
                    SectionPos.sectionRelative(pos.getZ()));

                if (PoiTypes.hasPoi(state))
                {
                    var type = PoiTypes.forState(state);
                    if (type.isPresent())
                    {
                        poiSection.add(pos, type.get());
                    }
                }
            }
        }
    }
}

