package xyz.brassgoggledcoders.transport.navigation;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.api.navigation.INavigator;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPoint;
import xyz.brassgoggledcoders.transport.api.navigation.NavigationPointType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public class ConnectorNavigationPoint extends NavigationPoint {
    private final Collection<UUID> connectedPoints;

    public ConnectorNavigationPoint(NavigationPointType pointType, BlockPos position) {
        super(pointType, position);
        this.connectedPoints = Sets.newHashSet();
    }

    @Override
    public boolean addConnectedPoint(NavigationPoint navigationPoint) {
        if (connectedPoints.size() < 2) {
            return connectedPoints.add(navigationPoint.getUniqueId());
        } else {
            return false;
        }
    }

    @Override
    public Collection<UUID> getConnectedPoints() {
        return connectedPoints;
    }

    @Override
    public void alertApproach(@Nonnull INavigator navigator, @Nonnull Entity entity) {
        super.alertApproach(navigator, entity);
        if (connectedPoints.contains(navigator.getLastPointId())) {

        }
    }
}
