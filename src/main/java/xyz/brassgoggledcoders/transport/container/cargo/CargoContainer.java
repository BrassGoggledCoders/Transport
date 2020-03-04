package xyz.brassgoggledcoders.transport.container.cargo;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.api.cargoinstance.EmptyCargoInstance;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

public class CargoContainer extends BasicInventoryContainer {
    private final CargoInstance cargoInstance;

    public CargoContainer(int containerId, CargoInstance cargoInstance, PlayerInventory playerInventory) {
        super(TransportContainers.LOADER.get(), playerInventory, containerId, IAssetProvider.DEFAULT_PROVIDER);
        this.cargoInstance = cargoInstance;
    }

    public CargoInstance getCargoInstance() {
        return cargoInstance;
    }

    public static CargoContainer create(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        Entity entity = playerInventory.player.getEntityWorld().getEntityByID(packetBuffer.readInt());
        if (entity != null) {
            LazyOptional<ICargoCarrier> carrierLazyOptional = entity.getCapability(TransportAPI.CARRIER_CAP);
            if (carrierLazyOptional.isPresent()) {

            }
            return new CargoContainer(id, new EmptyCargoInstance(), playerInventory);
        }
        throw new IllegalStateException("Failed to Find Loader Tile Entity");
    }
}
