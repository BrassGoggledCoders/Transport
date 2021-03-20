package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNetwork;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNetworks;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNodeType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class WayStationTileEntity extends TileEntity {
    private UUID wayStation;

    public WayStationTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void destroy() {
        RoutingNetwork network = RoutingNetworks.SHIP.getFor(this.getWorld());
        if (network != null) {
            RoutingNode routingNode = network.get(this.wayStation);
            if (routingNode != null) {
                network.remove(routingNode);
            }
        }
    }

    public void create() {
        RoutingNetwork network = RoutingNetworks.SHIP.getFor(this.getWorld());
        if (network != null) {
            RoutingNode routingNode = this.createWayStation();
            this.wayStation = routingNode.getUniqueId();
            network.add(routingNode);
            network.getNear(routingNode, 10)
                    .forEach(nearWayStation -> {
                        Transport.LOGGER.info("Connecting to " + nearWayStation.getType());
                        network.connect(routingNode, nearWayStation);
                    });
        }
    }

    protected RoutingNode createWayStation() {
        return new RoutingNode(this.getPos(), RoutingNodeType.WAY_STATION);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if (nbt.hasUniqueId("wayStation")) {
            this.wayStation = nbt.getUniqueId("wayStation");
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.putUniqueId("wayStation", this.wayStation);
        return super.write(compound);
    }
}
