package xyz.brassgoggledcoders.transport.loading;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.NonNullSupplier;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.loading.IEntityBlockLoading;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerMinecartIEntityBlockLoading implements IEntityBlockLoading {
    private final NonNullSupplier<LockableTileEntity> tileConstructor;

    public ContainerMinecartIEntityBlockLoading(NonNullSupplier<LockableTileEntity> tileConstructor) {
        this.tileConstructor = tileConstructor;
    }

    @Override
    public boolean attemptLoad(@Nonnull Entity entity, @Nonnull BlockState blockState, @Nullable TileEntity tileEntity) {
        return false;
    }

    @Nullable
    @Override
    public Pair<BlockState, TileEntity> attemptUnload(@Nonnull Entity entity) {
        if (entity instanceof ContainerMinecartEntity) {
            ContainerMinecartEntity containerMinecartEntity = (ContainerMinecartEntity) entity;
            BlockState chestState = containerMinecartEntity.getDefaultDisplayTile();
            LockableTileEntity tileEntity = tileConstructor.get();
            for (int i = 0; i < containerMinecartEntity.getSizeInventory(); i++) {
                tileEntity.setInventorySlotContents(i, containerMinecartEntity.getStackInSlot(i));
            }
            return Pair.of(chestState, tileEntity);
        }
        return null;
    }

    @Override
    public void unload(@Nonnull Entity entity) {
        if (entity instanceof ContainerMinecartEntity) {
            MinecartEntity minecartEntity = new MinecartEntity(entity.getEntityWorld(), entity.getPosX(), entity.getPosY(),
                    entity.getPosZ());
            ((ContainerMinecartEntity) entity).dropContentsWhenDead(false);

            CompoundNBT nbt = entity.writeWithoutTypeId(new CompoundNBT());
            nbt.remove("UUID");
            minecartEntity.read(nbt);
            entity.remove();
            entity.getEntityWorld().addEntity(minecartEntity);
        }

    }
}
