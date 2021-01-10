package xyz.brassgoggledcoders.transport.content;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;

public class TransportFluidTags {
    public static INamedTag<Fluid> STEAM = forgeTag("steam");

    public static INamedTag<Fluid> forgeTag(String path) {
        return FluidTags.createOptional(new ResourceLocation("forge", path));
    }
}
