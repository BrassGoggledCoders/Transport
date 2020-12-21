package xyz.brassgoggledcoders.transport.api.loading;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface IBlockEntityLoading {
    boolean attemptLoad(@Nonnull ILoading loading, @Nonnull BlockState blockState, @Nullable TileEntity tileEntity,
                       @Nonnull Entity entity);

    Collection<Block> getSupportedBlocks();
}
