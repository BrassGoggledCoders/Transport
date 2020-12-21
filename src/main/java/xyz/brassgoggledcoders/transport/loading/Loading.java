package xyz.brassgoggledcoders.transport.loading;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.transport.api.loading.ILoading;
import xyz.brassgoggledcoders.transport.block.EnderLoaderBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class Loading implements ILoading {
    private final BlockState loaderBlockState;

    public Loading(BlockState loaderBlockState) {
        this.loaderBlockState = loaderBlockState;
    }

    @Override
    @Nullable
    public <T extends Entity> T recreateEntity(EntityType<T> entityType, Entity originalEntity) {
        T entity = entityType.create(originalEntity.getEntityWorld());
        if (entity != null) {
            CompoundNBT data = originalEntity.writeWithoutTypeId(new CompoundNBT());
            data.remove("UUID");
            entity.read(data);
            return entity;
        }
        return null;
    }

    @Override
    public void swap(Entity added, Entity removed) {
        if (added.getUniqueID().equals(removed.getUniqueID())) {
            added.setUniqueId(UUID.randomUUID());
        }
        removed.remove();
        removed.getEntityWorld().addEntity(added);
    }

    @Override
    @Nonnull
    public Direction getLoaderFacing() {
        return this.loaderBlockState.get(EnderLoaderBlock.FACING);
    }
}
