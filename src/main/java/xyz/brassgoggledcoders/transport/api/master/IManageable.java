package xyz.brassgoggledcoders.transport.api.master;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IManageable extends INBTSerializable<CompoundNBT> {
    @Nonnull
    LazyOptional<IManager> getManager(IBlockReader blockReader);

    @Nullable
    BlockPos getManagerPos();

    void setManager(@Nonnull LazyOptional<IManager> manager);

    boolean isValidMaster(@Nonnull IManager manager);

    @Nonnull
    ManagedObject createManagedObject();
}
