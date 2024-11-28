package com.smoothchunk.mixin;

import com.smoothchunk.SmoothchunkMod;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ChunkMap.class)
public class ChunkMapSlowUnload
{
    @Shadow
    @Final
    @Mutable
    public LongSet toDrop = new LongLinkedOpenHashSet();

    @ModifyConstant(method = "processUnloads", constant = @Constant(intValue = 200))
    private int setLimit(final int constant)
    {
        return SmoothchunkMod.config.getCommonConfig().chunkUnloadLimit;
    }

    @ModifyConstant(method = "processUnloads", constant = @Constant(intValue = 2000))
    private int dropUntil(final int constant)
    {
        return 20000;
    }

    @Inject(method = "processUnloads", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ChunkMap;scheduleUnload(JLnet/minecraft/server/level/ChunkHolder;)V"))
    private void dropUntil(final BooleanSupplier p_140354_, final CallbackInfo ci)
    {
        SmoothchunkMod.LOGGER.warn("Unloading chunk");
    }
}
