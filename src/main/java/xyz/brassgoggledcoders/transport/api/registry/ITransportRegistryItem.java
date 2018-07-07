package xyz.brassgoggledcoders.transport.api.registry;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface ITransportRegistryItem {
    @Nonnull
    ResourceLocation getRegistryName();
}
