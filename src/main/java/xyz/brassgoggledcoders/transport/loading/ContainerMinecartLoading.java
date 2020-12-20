package xyz.brassgoggledcoders.transport.loading;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.NonNullSupplier;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.loading.IBlockEntityLoading;
import xyz.brassgoggledcoders.transport.api.loading.IEntityBlockLoading;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class ContainerMinecartLoading<T extends TileEntity & IInventory, E extends ContainerMinecartEntity>
        implements IEntityBlockLoading, IBlockEntityLoading {
    private final NonNullSupplier<T> tileConstructor;
    private final Supplier<Block> block;
    private final Supplier<EntityType<E>> entityType;

    public ContainerMinecartLoading(Block block, EntityType<E> entityType, NonNullSupplier<T> tileConstructor) {
        this(() -> block, () -> entityType, tileConstructor);
    }

    public ContainerMinecartLoading(Supplier<Block> block, Supplier<EntityType<E>> entityType,
                                    NonNullSupplier<T> tileConstructor) {
        this.block = block;
        this.entityType = entityType;
        this.tileConstructor = tileConstructor;
    }

    @Override
    @Nullable
    public Entity attemptLoad(@Nonnull Entity entity, @Nonnull CompoundNBT entityNBT,
                              @Nonnull BlockState blockState, @Nullable TileEntity tileEntity) {
        return null;
    }

    @Nullable
    @Override
    public Pair<BlockState, TileEntity> attemptUnload(@Nonnull Entity entity) {
        if (entity.getType() == entityType.get() && entity instanceof IInventory) {
            IInventory containerMinecartEntity = (IInventory) entity;
            T tileEntity = tileConstructor.get();
            int maxInventory = Math.min(tileEntity.getInventoryStackLimit(), containerMinecartEntity.getSizeInventory());
            for (int i = 0; i < maxInventory; i++) {
                tileEntity.setInventorySlotContents(i, containerMinecartEntity.removeStackFromSlot(i));
            }
            return Pair.of(block.get().getDefaultState(), tileEntity);
        }
        return null;
    }

    @Override
    public Entity unload(@Nonnull Entity entity, @Nonnull CompoundNBT entityNBT) {
        if (entity.getType() == entityType.get()) {
            MinecartEntity minecartEntity = new MinecartEntity(entity.getEntityWorld(), entity.getPosX(), entity.getPosY(),
                    entity.getPosZ());
            minecartEntity.read(entityNBT);
            return minecartEntity;
        } else {
            return null;
        }
    }

    @Override
    public Collection<EntityType<?>> getSupportedEntities() {
        return Collections.singleton(entityType.get());
    }

    @Override
    public Entity attemptLoad(@Nonnull BlockState blockState, @Nullable TileEntity tileEntity, @Nonnull Entity entity,
                              @Nonnull CompoundNBT entityNBT) {
        if (blockState.isIn(block.get()) && entity.getType() == EntityType.MINECART) {
            E newEntity = entityType.get().create(entity.getEntityWorld());
            if (newEntity != null) {
                newEntity.read(entityNBT);
                if (tileEntity instanceof IInventory) {
                    IInventory inventory = (IInventory) tileEntity;
                    int maxInventory = Math.min(newEntity.getSizeInventory(), inventory.getSizeInventory());
                    for (int i = 0; i < maxInventory; i++) {
                        newEntity.setInventorySlotContents(i, inventory.removeStackFromSlot(i));
                    }
                }
            }
            return newEntity;
        }
        return null;
    }

    @Override
    public Collection<Block> getSupportedBlocks() {
        return Collections.singleton(block.get());
    }
}
