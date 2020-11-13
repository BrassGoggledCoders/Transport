package xyz.brassgoggledcoders.transport.container.navigation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nonnull;

public class NavigationChartContainer extends Container {
    public NavigationChartContainer(ContainerType<?> containerType, int windowId) {
        super(containerType, windowId);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    public static NavigationChartContainer create(ContainerType<NavigationChartContainer> containerType, int windowId,
                                                  PlayerInventory inventory) {
        return new NavigationChartContainer(containerType, windowId);
    }

    public static NavigationChartContainer create(int windowId, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new NavigationChartContainer(TransportContainers.NAVIGATION_CHART.get(), windowId);
    }
}
