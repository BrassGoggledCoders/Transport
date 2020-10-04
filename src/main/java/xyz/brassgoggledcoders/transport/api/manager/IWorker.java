package xyz.brassgoggledcoders.transport.api.manager;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IWorker extends INBTSerializable<CompoundNBT> {
    @Nonnull
    LazyOptional<IManager> getManager(IBlockReader blockReader);

    @Nullable
    BlockPos getManagerPos();

    @Nonnull
    BlockPos getWorkerPos();

    void setManagerPos(@Nullable BlockPos blockPos);

    boolean isValidManager(@Nonnull IManager manager);

    @Nonnull
    ItemStack getRepresentative();

    @Nonnull
    UUID getUniqueId();
}
