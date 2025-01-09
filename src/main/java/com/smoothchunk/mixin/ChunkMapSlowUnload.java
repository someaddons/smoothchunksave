package com.smoothchunk.mixin;

import com.smoothchunk.SmoothchunkMod;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.function.BooleanSupplier;

@Mixin(ChunkMap.class)
public class ChunkMapSlowUnload
{
    @Shadow
    @Final
    @Mutable
    public LongSet toDrop = new LongLinkedOpenHashSet();

    @Unique
    private int scheduledToUnloadAmount = 0;

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

    @Inject(method = "scheduleUnload", at = @At("HEAD"))
    private void onSchedule(final long p_140182_, final ChunkHolder p_140183_, final CallbackInfo ci)
    {
        scheduledToUnloadAmount++;
    }

    @Inject(method = "processUnloads", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V"))
    private void onRunUnload(final BooleanSupplier p_140354_, final CallbackInfo ci)
    {
        scheduledToUnloadAmount = Math.max(0, scheduledToUnloadAmount - 1);
    }

    @Redirect(method = "processUnloads", at = @At(value = "INVOKE", target = "Ljava/util/Queue;size()I"))
    private int getSize(final Queue instance)
    {
        return scheduledToUnloadAmount;
    }
}
