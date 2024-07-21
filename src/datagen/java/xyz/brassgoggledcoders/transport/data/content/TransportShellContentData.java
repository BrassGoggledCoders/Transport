package xyz.brassgoggledcoders.transport.data.content;

import net.minecraft.world.level.block.Blocks;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.codec.CodecProviderType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.StorageSize;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportShellContent;
import xyz.brassgoggledcoders.transport.data.provider.shellcontent.ShellContentInfoBuilder;
import xyz.brassgoggledcoders.transport.data.provider.shellcontent.builder.EnergyStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.data.provider.shellcontent.builder.FluidStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.data.provider.shellcontent.builder.ItemStorageShellContentBuilder;

public class TransportShellContentData {
    public static void generate(DataRegistering dataRegistering) {
        dataRegistering.getProvider(CodecProviderType.get(TransportShellContent.REGISTRY_KEY));
    }

    public static void generate(RegistrateShellContentDataProvider dataProvider) {
        ShellContentInfoBuilder.of(Blocks.BARREL)
                .withShellContentCreator(ItemStorageShellContentBuilder.of(StorageSize.THREE_BY_NINE))
                .build(Transport.rl("barrel"), dataProvider);

        ShellContentInfoBuilder.of(Blocks.CHEST)
                .withShellContentCreator(ItemStorageShellContentBuilder.of(StorageSize.THREE_BY_NINE))
                .build(Transport.rl("chest"), dataProvider);

        ShellContentInfoBuilder.of(TransportBlocks.FLUID_STORAGE.get())
                .withShellContentCreator(FluidStorageShellContentBuilder.ofBuckets(50))
                .build(Transport.rl("fluid_storage"), dataProvider);

        ShellContentInfoBuilder.of(TransportBlocks.ENERGY_STORAGE.get())
                .withShellContentCreator(EnergyStorageShellContentBuilder.of(100000))
                .build(Transport.rl("energy_storage"), dataProvider);
    }
}
