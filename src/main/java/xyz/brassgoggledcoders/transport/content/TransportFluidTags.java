package xyz.brassgoggledcoders.transport.content;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class TransportFluidTags {
    public static final ITag.INamedTag<Fluid> STEAM = FluidTags.createOptional(
            new ResourceLocation("forge", "steam"));
}
