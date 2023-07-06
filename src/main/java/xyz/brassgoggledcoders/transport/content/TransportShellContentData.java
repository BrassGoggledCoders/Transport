package xyz.brassgoggledcoders.transport.content;

import net.minecraft.world.level.block.Blocks;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.StorageSize;
import xyz.brassgoggledcoders.transport.data.shellcontent.RegistrateShellContentDataProvider;
import xyz.brassgoggledcoders.transport.data.shellcontent.ShellContentInfoBuilder;
import xyz.brassgoggledcoders.transport.data.shellcontent.builder.EnergyStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.data.shellcontent.builder.FluidStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.data.shellcontent.builder.ItemStorageShellContentBuilder;

public class TransportShellContentData {
    public static void generateData(RegistrateShellContentDataProvider dataProvider) {
        ShellContentInfoBuilder.of(Blocks.BARREL)
                .withShellContentCreator(ItemStorageShellContentBuilder.of(StorageSize.THREE_BY_NINE))
                .build(Transport.rl("barrel"), dataProvider);

        ShellContentInfoBuilder.of(TransportBlocks.FLUID_STORAGE.get())
                .withShellContentCreator(FluidStorageShellContentBuilder.ofBuckets(50))
                .build(Transport.rl("fluid_storage"), dataProvider);

        ShellContentInfoBuilder.of(TransportBlocks.ENERGY_STORAGE.get())
                .withShellContentCreator(EnergyStorageShellContentBuilder.of(100000))
                .build(Transport.rl("energy_storage"), dataProvider);
    }
}
