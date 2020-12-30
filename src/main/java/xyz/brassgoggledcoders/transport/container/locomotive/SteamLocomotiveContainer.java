package xyz.brassgoggledcoders.transport.container.locomotive;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.transport.container.slot.FuelSlot;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.entity.EntityWorldPosCallable;
import xyz.brassgoggledcoders.transport.entity.locomotive.SteamLocomotiveEntity;
import xyz.brassgoggledcoders.transport.network.property.IPropertyManaged;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;
import xyz.brassgoggledcoders.transport.network.property.PropertyTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

public class SteamLocomotiveContainer extends Container implements IPropertyManaged {
    private final IWorldPosCallable worldPosCallable;

    private final PropertyManager propertyManager;
    private final Property<Boolean> on;
    private final Property<Integer> burnRemaining;
    private final Property<Integer> maxBurn;

    public SteamLocomotiveContainer(@Nullable ContainerType<?> type, int windowId, PlayerInventory playerInventory) {
        super(type, windowId);
        this.propertyManager = new PropertyManager((short) windowId);
        this.on = this.propertyManager.addTrackedProperty(PropertyTypes.BOOLEAN.create());
        this.burnRemaining = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.maxBurn = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.worldPosCallable = IWorldPosCallable.DUMMY;
        this.addSlots(new ItemStackHandler(1), playerInventory);
    }

    public SteamLocomotiveContainer(int windowId, PlayerInventory playerInventory, SteamLocomotiveEntity locomotiveEntity) {
        super(TransportContainers.STEAM_LOCOMOTIVE.get(), windowId);

        this.propertyManager = new PropertyManager((short) windowId);
        this.on = this.propertyManager.addTrackedProperty(PropertyTypes.BOOLEAN.create(
                locomotiveEntity::isOn,
                locomotiveEntity::setOn
        ));
        this.burnRemaining = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                locomotiveEntity.getSteamEngine()::getBurnRemaining
        ));
        this.maxBurn = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                locomotiveEntity.getSteamEngine()::getMaxBurn
        ));

        this.worldPosCallable = new EntityWorldPosCallable(locomotiveEntity);
        this.addSlots(locomotiveEntity.getSteamEngine().getFuelHandler(), playerInventory);
    }

    public void addSlots(IItemHandler handler, PlayerInventory playerInventory) {
        this.addSlot(new FuelSlot(handler, 0, 11, 53));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 85 + i * 198));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 143));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.worldPosCallable.applyOrElse((world, blockPos) ->
                        player.getDistanceSq((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D,
                                (double) blockPos.getZ() + 0.5D) <= 64.0D,
                true);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.propertyManager.sendChanges(this.listeners, false);
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        this.propertyManager.sendChanges(Collections.singletonList(listener), true);
    }

    public boolean isLocomotiveOn() {
        return on.get() == Boolean.TRUE;
    }

    public int getFuelBurnedScaled() {
        Integer maxBurnValue = this.maxBurn.get();
        Integer burnRemainingValue = this.burnRemaining.get();
        if (maxBurnValue != null && burnRemainingValue != null && maxBurnValue > 0) {
            int burned = maxBurnValue - burnRemainingValue;
            float percentBurned = (float) burned / maxBurnValue;
            return (int) Math.ceil(percentBurned * 10);
        } else {
            return 10;
        }
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
