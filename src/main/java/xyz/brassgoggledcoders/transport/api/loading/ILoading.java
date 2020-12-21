package xyz.brassgoggledcoders.transport.api.loading;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ILoading {
    @Nullable
    <E extends Entity> E recreateEntity(EntityType<E> entityType, Entity originalEntity);

    void swap(Entity added, Entity removed);

    @Nonnull
    Direction getLoaderFacing();
}
