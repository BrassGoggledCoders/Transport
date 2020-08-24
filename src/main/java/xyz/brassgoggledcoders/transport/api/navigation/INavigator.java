package xyz.brassgoggledcoders.transport.api.navigation;

import javax.annotation.Nullable;

public interface INavigator {
    @Nullable
    INavigationPoint getCurrentPoint(INavigationNetwork network);

    @Nullable
    INavigationPoint getLastPoint(INavigationNetwork network);

    void setCurrentPoint(@Nullable INavigationPoint point);
}
