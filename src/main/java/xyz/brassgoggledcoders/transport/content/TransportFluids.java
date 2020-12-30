package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import xyz.brassgoggledcoders.transport.Transport;

public class TransportFluids {
    public static final FluidEntry<Flowing> STEAM = Transport.getRegistrate()
            .object("steam")
            .fluid()
            .attributes(builder -> builder.temperature(1000)
                    .viscosity(200)
                    .gaseous())
            .tag(TransportFluidTags.STEAM)
            .bucket()
            .model((context, provider) -> DynamicBucketModelBuilder.begin(provider.getBuilder("item/steam_bucket"), provider.existingFileHelper)
                    .fluid(context.get().getFluid())
                    .end()
            )
            .build()
            .register();

    public static void setup() {

    }
}
