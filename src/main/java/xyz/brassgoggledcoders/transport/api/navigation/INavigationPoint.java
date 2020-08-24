package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface INavigationPoint extends INBTSerializable<CompoundNBT> {
    @Nonnull
    BlockPos getPosition();

    @Nonnull
    UUID getUniqueId();

    void alertApproach(@Nonnull INavigator navigator, @Nonnull Entity entity);
}
