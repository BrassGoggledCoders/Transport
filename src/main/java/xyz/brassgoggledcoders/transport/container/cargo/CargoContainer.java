package xyz.brassgoggledcoders.transport.container.cargo;

import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddonProvider;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nonnull;

public class CargoContainer extends BasicInventoryContainer {
    private final CargoInstance cargoInstance;
    private int addonSlots = 0;

    public CargoContainer(int containerId, CargoInstance cargoInstance, PlayerInventory playerInventory) {
        super(TransportContainers.CARGO.get(), playerInventory, containerId, IAssetProvider.DEFAULT_PROVIDER);
        this.cargoInstance = cargoInstance;
        if (cargoInstance instanceof IContainerAddonProvider) {
            ((IContainerAddonProvider) cargoInstance).getContainerAddons()
                    .forEach(containerAddon -> {
                        containerAddon.getSlots(this).forEach(this::addAddonSlot);
                        containerAddon.getTrackedInts().forEach(this::trackInt);
                        containerAddon.getTrackedIntArrays().forEach(this::trackIntArray);
                    });
        }
        this.initInventory();
    }

    private void addAddonSlot(Slot slot) {
        this.addSlot(slot);
        this.addonSlots++;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public CargoInstance getCargoInstance() {
        return cargoInstance;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemstack = slotStack.copy();
            if (index < addonSlots) {
                if (!this.mergeItemStack(slotStack, addonSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 0, addonSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public static CargoContainer create(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        Entity entity = playerInventory.player.getEntityWorld().getEntityByID(packetBuffer.readInt());
        if (entity instanceof ICargoCarrier) {
            return new CargoContainer(id, ((ICargoCarrier) entity).getCargoInstance(), playerInventory);
        }
        throw new IllegalStateException("Failed to Find Loader Tile Entity");
    }
}
