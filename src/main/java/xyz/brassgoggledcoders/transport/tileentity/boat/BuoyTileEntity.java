package xyz.brassgoggledcoders.transport.tileentity.boat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.api.TransportCapabilities;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationNetwork;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationPoint;
import xyz.brassgoggledcoders.transport.content.TransportNavigationPoints;
import xyz.brassgoggledcoders.transport.navigation.NavigationNetwork;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class BuoyTileEntity extends TileEntity {
    private UUID navigationPointUniqueId;

    public BuoyTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void interact(PlayerEntity playerEntity) {
        if (navigationPointUniqueId == null) {
            create(playerEntity);
        }

        if (navigationPointUniqueId != null && this.getWorld() != null) {
            this.getWorld().getCapability(TransportCapabilities.NAVIGATION_NETWORK)
                    .ifPresent(network -> {
                        INavigationPoint navigationPoint = network.getNavigationPoint(navigationPointUniqueId);
                        if (navigationPoint != null) {
                            network.setKnownNavigationPoint(playerEntity, navigationPoint, true);
                        }
                    });
        }
    }

    public void setup(@Nullable PlayerEntity playerEntity) {
        create(playerEntity);
    }

    public void destroy() {
        if (navigationPointUniqueId != null && this.getWorld() != null) {
            this.getWorld().getCapability(TransportCapabilities.NAVIGATION_NETWORK)
                    .ifPresent(network -> network.removeNavigationPoint(navigationPointUniqueId));
        }
    }

    protected void create(@Nullable PlayerEntity playerEntity) {
        if (this.getWorld() != null) {
            this.navigationPointUniqueId = this.getWorld().getCapability(TransportCapabilities.NAVIGATION_NETWORK)
                    .resolve()
                    .flatMap(network -> this.createNavigationPoint(network)
                            .map(navigationPoint -> {
                                network.addNavigationPoint(navigationPoint);
                                navigationPoint.setPosition(this.getPos());
                                if (playerEntity != null) {
                                    network.setKnownNavigationPoint(playerEntity, navigationPoint, true);
                                }
                                return navigationPoint.getUniqueId();
                            })
                    )
                    .orElse(null);
        }
    }

    protected Optional<INavigationPoint> createNavigationPoint(INavigationNetwork navigationNetwork) {
        return TransportNavigationPoints.RANDOM.map(navigationNetwork::createPoint);
    }
}
