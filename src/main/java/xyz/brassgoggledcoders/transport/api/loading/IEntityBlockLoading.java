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
    @Nullable
    Entity attemptLoad(@Nonnull Entity entity, @Nonnull CompoundNBT entityNBT, @Nonnull BlockState blockState,
                        @Nullable TileEntity tileEntity);

    @Nullable
    Pair<BlockState, TileEntity> attemptUnload(@Nonnull Entity entity);

    @Nullable
    Entity unload(@Nonnull Entity entity, @Nonnull CompoundNBT entityNBT);

    Collection<EntityType<?>> getSupportedEntities();
}
