package xyz.brassgoggledcoders.transport.api.navigation;

import javax.annotation.Nullable;

public class Navigator implements INavigator {
    private INavigationPoint currentPoint;
    private INavigationPoint lastPoint;

    @Nullable
    @Override
    public INavigationPoint getCurrentPoint(INavigationNetwork network) {
        return this.currentPoint;
    }

    @Nullable
    @Override
    public INavigationPoint getLastPoint(INavigationNetwork network) {
        return this.lastPoint;
    }

    @Override
    public void setCurrentPoint(@Nullable INavigationPoint point) {
        this.lastPoint = this.currentPoint;
        this.currentPoint = point;
    }
}
