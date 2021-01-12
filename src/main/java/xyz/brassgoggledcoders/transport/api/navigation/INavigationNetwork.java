package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface INavigationNetwork extends INBTSerializable<CompoundNBT> {
    @Nonnull
    Collection<INavigationPoint> getNavigationPoints();

    boolean addNavigationPoint(@Nonnull INavigationPoint navigationPoint);

    @Nullable
    INavigationPoint removeNavigationPoint(@Nonnull UUID uniqueID);

    @Nullable
    INavigationPoint getNavigationPoint(@Nonnull UUID uniqueID);

    @Nonnull
    Collection<INavigationPoint> getKnownNavigationPoints(@Nonnull PlayerEntity playerEntity);

    boolean setKnownNavigationPoint(@Nonnull PlayerEntity playerEntity, @Nonnull INavigationPoint navigationPoint, boolean known);

    @Nonnull
    INavigationPoint createPoint(@Nonnull NavigationPointType pointType);
}
