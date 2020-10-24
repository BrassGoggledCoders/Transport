package xyz.brassgoggledcoders.transport.navigation;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationNetwork;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class NavigationNetwork implements INavigationNetwork {
    private final Map<UUID, INavigationPoint> navigationPoints;

    public NavigationNetwork() {
        this.navigationPoints = Maps.newHashMap();
    }

    @Nonnull
    @Override
    public Map<UUID, INavigationPoint> getNavigationPoints() {
        return navigationPoints;
    }

    @Override
    public boolean addNavigationPoint(@Nonnull INavigationPoint navigationPoint) {
        if (!navigationPoints.containsKey(navigationPoint.getUniqueId())) {
            navigationPoints.put(navigationPoint.getUniqueId(), navigationPoint);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public INavigationPoint removeNavigationPoint(@Nonnull UUID uniqueId) {
        return navigationPoints.remove(uniqueId);
    }

    @Nullable
    @Override
    public INavigationPoint getNavigationPoint(@Nonnull UUID uniqueId) {
        return navigationPoints.get(uniqueId);
    }

    @Nonnull
    @Override
    public Collection<INavigationPoint> getKnownNavigationPoints(@Nonnull PlayerEntity playerEntity) {
        return navigationPoints.values();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
