package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import xyz.brassgoggledcoders.transport.Transport;

@SuppressWarnings("UnstableApiUsage")
public class TransportFluids {
    public static RegistryEntry<ForgeFlowingFluid.Flowing> STEAM = Transport.getRegistrate()
            .object("steam")
            .fluid()
            .lang(flowing -> flowing.getAttributes().getTranslationKey(), "Steam")
            .removeTag(FluidTags.WATER)
            .tag(TransportFluidTags.STEAM)
            .noBucket()
            .register();

    public static void setup() {

    }
}
