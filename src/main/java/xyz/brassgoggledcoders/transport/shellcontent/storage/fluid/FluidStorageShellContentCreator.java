package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.IShellContentCreator;

public record FluidStorageShellContentCreator(
        BlockState blockState,
        int capacity
) implements IShellContentCreator<FluidStorageShellContent> {
    public static Codec<FluidStorageShellContentCreator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.optionalFieldOf("blockState", Blocks.AIR.defaultBlockState())
                    .forGetter(FluidStorageShellContentCreator::blockState),
            Codec.INT.fieldOf("capacity")
                    .forGetter(FluidStorageShellContentCreator::capacity)
    ).apply(instance, FluidStorageShellContentCreator::new));

    @NotNull
    @Override
    public FluidStorageShellContent get() {
        return new FluidStorageShellContent(blockState, capacity);
    }
}
