package xyz.brassgoggledcoders.transport.container.cargo;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddonProvider;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

public class CargoContainer extends BasicInventoryContainer {
    private final CargoInstance cargoInstance;

    public CargoContainer(int containerId, CargoInstance cargoInstance, PlayerInventory playerInventory) {
        super(TransportContainers.CARGO.get(), playerInventory, containerId, IAssetProvider.DEFAULT_PROVIDER);
        this.cargoInstance = cargoInstance;
        if (cargoInstance instanceof IContainerAddonProvider) {
            ((IContainerAddonProvider) cargoInstance).getContainerAddons()
                    .forEach(containerAddon -> {
                        containerAddon.getSlots(this).forEach(this::addSlot);
                        containerAddon.getTrackedInts().forEach(this::trackInt);
                        containerAddon.getTrackedIntArrays().forEach(this::trackIntArray);
                    });
        }
        this.addPlayerChestInventory();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public CargoInstance getCargoInstance() {
        return cargoInstance;
    }

    public static CargoContainer create(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        Entity entity = playerInventory.player.getEntityWorld().getEntityByID(packetBuffer.readInt());
        if (entity instanceof ICargoCarrier) {
            return new CargoContainer(id, ((ICargoCarrier) entity).getCargoInstance(), playerInventory);
        }
        throw new IllegalStateException("Failed to Find Loader Tile Entity");
    }
}
