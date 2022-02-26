package com.smoothchunk.mixin;

import com.smoothchunk.SmoothchunkMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MinecraftServerSaveMixin
{
    @Redirect(method = "saveEverything", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;saveAllChunks(ZZZ)Z"))
    public boolean onSaveALlChunks(final MinecraftServer server, boolean a, boolean b, boolean c)
    {
        if (SmoothchunkMod.config.getCommonConfig().noSaveAll.get())
        {
            return false;
        }
        else
        {
            return server.saveAllChunks(a, b, c);
        }
    }
}
