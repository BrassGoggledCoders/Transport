package xyz.brassgoggledcoders.transport.tileentity.boat;

import net.minecraft.tileentity.TileEntityType;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNodeType;
import xyz.brassgoggledcoders.transport.tileentity.WayStationTileEntity;

public class DockTileEntity extends WayStationTileEntity {
    public DockTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    protected RoutingNode createWayStation() {
        return new RoutingNode(this.getPos(), RoutingNodeType.STATION);
    }
}
