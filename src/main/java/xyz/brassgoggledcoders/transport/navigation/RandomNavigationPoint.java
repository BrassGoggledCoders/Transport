package xyz.brassgoggledcoders.transport.navigation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.api.TransportCapabilities;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationPoint;
import xyz.brassgoggledcoders.transport.api.navigation.INavigator;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPoint;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPointType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class RandomNavigationPoint extends NavigationPoint {
    public RandomNavigationPoint(NavigationPointType pointType, BlockPos position) {
        super(pointType, position);
    }

    @Override
    public boolean addConnectedPoint(NavigationPoint navigationPoint) {
        return false;
    }

    @Override
    public Collection<UUID> getConnectedPoints() {
        return Collections.emptyList();
    }

    @Override
    public void alertApproach(@Nonnull INavigator navigator, @Nonnull Entity entity) {
        super.alertApproach(navigator, entity);
        entity.getEntityWorld().getCapability(TransportCapabilities.NAVIGATION_NETWORK)
                .ifPresent(network -> {
                    Collection<INavigationPoint> navigationPoints = network.getNavigationPoints();
                    if (navigationPoints.size() > 1) {
                        INavigationPoint randomPoint = navigationPoints.toArray(new INavigationPoint[0])
                                [entity.getEntityWorld().rand.nextInt(navigationPoints.size())];
                        navigator.setCurrentPoint(randomPoint);
                    }
                });
    }
}
