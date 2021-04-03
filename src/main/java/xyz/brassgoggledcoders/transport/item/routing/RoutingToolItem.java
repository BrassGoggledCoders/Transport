package xyz.brassgoggledcoders.transport.item.routing;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.container.NamedContainerProvider;
import xyz.brassgoggledcoders.transport.container.routing.RoutingToolContainer;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingEdge;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNetwork;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNetworks;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;
import xyz.brassgoggledcoders.transport.tileentity.WayStationTileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class RoutingToolItem extends Item {
    public RoutingToolItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
        if (tileEntity instanceof WayStationTileEntity) {
            RoutingNode routingNode = ((WayStationTileEntity) tileEntity).getRoutingNode();
            if (routingNode != null && context.getPlayer() instanceof ServerPlayerEntity) {
                RoutingNetwork routingNetwork = RoutingNetworks.SHIP.getFor(context.getWorld());
                if (routingNetwork != null) {
                    List<Pair<RoutingNode, RoutingEdge>> neighbors = routingNetwork.getNeighborsWithEdges(routingNode);
                    NetworkHooks.openGui((ServerPlayerEntity) context.getPlayer(),
                            new NamedContainerProvider(
                                    this.getDisplayName(context.getItem()),
                                    (id, inventory, player) -> new RoutingToolContainer(
                                            TransportContainers.ROUTING_TOOL.get(),
                                            id,
                                            inventory,
                                            IWorldPosCallable.of(context.getWorld(), context.getPos()),
                                            routingNode,
                                            neighbors
                                    )
                            ),
                            packetBuffer -> {
                                routingNode.toBuffer(packetBuffer);
                                packetBuffer.writeInt(neighbors.size());
                                for (Pair<RoutingNode, RoutingEdge> neighbor : neighbors) {
                                    neighbor.getLeft().toBuffer(packetBuffer);
                                    neighbor.getRight().toBuffer(packetBuffer);
                                }
                            });
                }
            }
            return ActionResultType.func_233537_a_(context.getWorld().isRemote());
        }
        return super.onItemUse(context);
    }
}
