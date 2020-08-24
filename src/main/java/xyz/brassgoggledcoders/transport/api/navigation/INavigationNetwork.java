package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface INavigationNetwork {
    @Nonnull
    Map<UUID, INavigationPoint> getNavigationPoints();

    @Nullable
    INavigationPoint getNavigationPoint(@Nonnull UUID uniqueID);

    @Nonnull
    List<INavigationPoint> getKnownNavigationPoints(@Nonnull PlayerEntity playerEntity);
}
