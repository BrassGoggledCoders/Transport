package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
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
            .bucket()
            .properties((p) -> p.containerItem(Items.BUCKET).maxStackSize(1))
            .model((context, provider) -> provider.withExistingParent(context.getId().getPath(), new ResourceLocation("forge", "item/bucket"))
                    .customLoader(DynamicBucketModelBuilder::begin)
                    .fluid(context.get().getFluid()))
            .lang("Steam Bucket")
            .build()
            .register();

    public static void setup() {

    }
}
