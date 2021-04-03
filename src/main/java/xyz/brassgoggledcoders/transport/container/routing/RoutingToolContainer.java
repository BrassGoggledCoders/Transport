package xyz.brassgoggledcoders.transport.container.routing;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.container.BasicContainer;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingEdge;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;

import javax.annotation.Nullable;
import java.util.List;

public class RoutingToolContainer extends BasicContainer {
    private final List<Pair<RoutingNode, RoutingEdge>> neighbors;
    private final RoutingNode opened;

    public RoutingToolContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                                IWorldPosCallable worldPosCallable, RoutingNode opened,
                                List<Pair<RoutingNode, RoutingEdge>> neighbors) {
        super(type, id, worldPosCallable);

        this.opened = opened;
        this.neighbors = neighbors;

        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot, 108, 84);
    }

    public RoutingNode getOpened() {
        return opened;
    }

    public List<Pair<RoutingNode, RoutingEdge>> getNeighbors() {
        return neighbors;
    }
}
