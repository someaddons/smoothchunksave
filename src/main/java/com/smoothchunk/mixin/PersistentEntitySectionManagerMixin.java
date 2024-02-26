package com.smoothchunk.mixin;

import com.smoothchunk.SmoothchunkMod;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PersistentEntitySectionManager.class, priority = 200)
public abstract class PersistentEntitySectionManagerMixin
{
    @Shadow
    protected abstract void processUnloads();

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;processUnloads()V"))
    public void processUnloadsSafe(final PersistentEntitySectionManager instance)
    {
        try
        {
            processUnloads();
        }
        catch (Exception e)
        {
            SmoothchunkMod.LOGGER.warn("Caught error on unload chunk during processing entities:", e);
        }
    }
}
