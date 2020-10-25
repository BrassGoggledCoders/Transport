package xyz.brassgoggledcoders.transport.api.navigation;

import javax.annotation.Nullable;
import java.util.UUID;

public interface INavigator {
    @Nullable
    default INavigationPoint getCurrentPoint(INavigationNetwork network) {
        UUID currentPointId = this.getCurrentPointId();
        if (currentPointId != null) {
            return network.getNavigationPoint(currentPointId);
        } else {
            return null;
        }
    }

    @Nullable
    UUID getCurrentPointId();

    @Nullable
    default INavigationPoint getLastPoint(INavigationNetwork network) {
        UUID lastPointId = this.getLastPointId();
        if (lastPointId != null) {
            return network.getNavigationPoint(lastPointId);
        } else {
            return null;
        }
    }

    @Nullable
    UUID getLastPointId();

    void setCurrentPoint(@Nullable INavigationPoint point);
}
