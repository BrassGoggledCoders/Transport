package xyz.brassgoggledcoders.transport.navigation;

import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.transport.api.navigation.*;

import javax.annotation.Nonnull;
import java.util.Collection;

public class RandomNavigationPoint extends NavigationPoint {
    public RandomNavigationPoint(INavigationNetwork navigationNetwork, NavigationPointType pointType) {
        super(navigationNetwork, pointType);
    }

    @Override
    public void alertApproach(@Nonnull INavigator navigator, @Nonnull Entity entity) {
        super.alertApproach(navigator, entity);

        Collection<INavigationPoint> navigationPoints = this.getNetwork().getNavigationPoints();
        if (navigationPoints.size() > 1) {
            INavigationPoint randomPoint = navigationPoints.toArray(new INavigationPoint[0])
                    [entity.getEntityWorld().rand.nextInt(navigationPoints.size())];
            navigator.setCurrentPoint(randomPoint);
        }
    }
}
