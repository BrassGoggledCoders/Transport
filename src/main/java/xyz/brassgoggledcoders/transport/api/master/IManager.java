package xyz.brassgoggledcoders.transport.api.master;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public interface IManager extends INBTSerializable<CompoundNBT> {
    @Nonnull
    BlockPos getPosition();

    @Nonnull
    UUID getUniqueId();

    @Nonnull
    ManagerType getType();

    boolean addManagedObject(@Nonnull ManagedObject managedObject);

    @Nonnull
    Collection<ManagedObject> getManagedObjects();

    @Nonnull
    AxisAlignedBB getBoundary();
}
