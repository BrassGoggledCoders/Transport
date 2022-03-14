package xyz.brassgoggledcoders.transport.block.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.blockentity.storage.BlockEntityCapabilityStorage;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public class BlockCapabilityStorage<T extends BlockEntityCapabilityStorage<?, ?>> extends Block implements EntityBlock {
    private final BiFunction<BlockPos, BlockState, T> blockEntitySupplier;

    public BlockCapabilityStorage(Properties properties, BiFunction<BlockPos, BlockState, T> blockEntityCreator) {
        super(properties);
        this.blockEntitySupplier = blockEntityCreator;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntitySupplier.apply(pPos, pState);
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof BlockEntityCapabilityStorage blockEntityCapabilityStorage) {
            return blockEntityCapabilityStorage.getAnalogOutputSignal();
        }
        return 0;
    }
}
