package xyz.brassgoggledcoders.transport.api.loading;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface IEntityBlockLoading {
    boolean attemptLoad(@Nonnull ILoading loading, @Nonnull Entity entity, @Nonnull BlockState blockState,
                        @Nullable TileEntity tileEntity);

    @Nullable
    Pair<BlockState, TileEntity> attemptUnload(@Nonnull Entity entity);

    void unload(@Nonnull ILoading loading, @Nonnull Entity entity);

    Collection<EntityType<?>> getSupportedEntities();
}
