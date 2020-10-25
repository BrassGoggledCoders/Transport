package xyz.brassgoggledcoders.transport.api.navigation;

import javax.annotation.Nullable;
import java.util.UUID;

public class Navigator implements INavigator {
    private UUID currentPointUniqueId;
    private UUID lastPointUniqueId;

    @Nullable
    @Override
    public UUID getCurrentPointId() {
        return currentPointUniqueId;
    }

    @Nullable
    @Override
    public UUID getLastPointId() {
        return lastPointUniqueId;
    }

    @Override
    public void setCurrentPoint(@Nullable INavigationPoint point) {
        this.lastPointUniqueId = this.currentPointUniqueId;
        this.currentPointUniqueId = point != null ? point.getUniqueId() : null;
    }
}
