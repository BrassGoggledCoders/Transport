package xyz.brassgoggledcoders.transport.api.loading;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IEntityBlockLoading {
    boolean attemptLoad(@Nonnull Entity entity, @Nonnull BlockState blockState,
                        @Nullable TileEntity tileEntity);

    @Nullable
    Pair<BlockState, TileEntity> attemptUnload(@Nonnull Entity entity);

    void unload(@Nonnull Entity entity);
}
