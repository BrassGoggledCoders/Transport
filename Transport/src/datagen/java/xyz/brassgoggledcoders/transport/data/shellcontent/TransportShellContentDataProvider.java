package xyz.brassgoggledcoders.transport.data.shellcontent;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.StorageSize;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.data.shellcontent.builder.EnergyStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.data.shellcontent.builder.FluidStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.data.shellcontent.builder.ItemStorageShellContentBuilder;

import java.util.Collection;
import java.util.function.BiConsumer;

public class TransportShellContentDataProvider extends ShellContentDataProvider {
    public TransportShellContentDataProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void gather(BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> consumer) {
        ShellContentInfoBuilder.of(Blocks.BARREL)
                .withShellContentCreator(ItemStorageShellContentBuilder.of(StorageSize.THREE_BY_NINE))
                .build(consumer);

        ShellContentInfoBuilder.of(TransportBlocks.FLUID_STORAGE.get())
                .withShellContentCreator(FluidStorageShellContentBuilder.ofBuckets(50))
                .build(consumer);

        ShellContentInfoBuilder.of(TransportBlocks.ENERGY_STORAGE.get())
                .withShellContentCreator(EnergyStorageShellContentBuilder.of(100000))
                .build(consumer);
    }

    @NotNull
    @Override
    public String getName() {
        return super.getName();
    }
}
