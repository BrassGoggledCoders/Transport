package xyz.brassgoggledcoders.transport.container.locomotive;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.container.slot.FuelSlot;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.entity.EntityWorldPosCallable;
import xyz.brassgoggledcoders.transport.entity.locomotive.SteamLocomotiveEntity;
import xyz.brassgoggledcoders.transport.network.property.IPropertyManaged;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;
import xyz.brassgoggledcoders.transport.network.property.PropertyTypes;
import xyz.brassgoggledcoders.transport.util.WorldHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

public class SteamLocomotiveContainer extends Container implements IPropertyManaged {
    private final IWorldPosCallable worldPosCallable;

    private final PropertyManager propertyManager;
    private final Property<Boolean> on;
    private final Property<Integer> burnRemaining;
    private final Property<Integer> maxBurn;
    private final Property<FluidStack> water;
    private final Property<Integer> speed;
    private final Property<Double> steam;

    public SteamLocomotiveContainer(@Nullable ContainerType<?> type, int windowId, PlayerInventory playerInventory) {
        super(type, windowId);
        this.propertyManager = new PropertyManager((short) windowId);
        this.on = this.propertyManager.addTrackedProperty(PropertyTypes.BOOLEAN.create());
        this.burnRemaining = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.maxBurn = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.water = this.propertyManager.addTrackedProperty(PropertyTypes.FLUID_STACK.create());
        this.speed = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        this.steam = this.propertyManager.addTrackedProperty(PropertyTypes.DOUBLE.create());
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
                locomotiveEntity.getEngine()::getBurnRemaining
        ));
        this.maxBurn = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                locomotiveEntity.getEngine()::getMaxBurn
        ));

        this.water = this.propertyManager.addTrackedProperty(PropertyTypes.FLUID_STACK.create(
                locomotiveEntity.getEngine().getWaterTank()::getFluid
        ));

        this.speed = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                () -> locomotiveEntity.getEngineState().ordinal(),
                id -> locomotiveEntity.alterEngineState(EngineState.byId(id))
        ));
        this.steam = this.propertyManager.addTrackedProperty(PropertyTypes.DOUBLE.create(
                locomotiveEntity.getEngine()::getSteam
        ));

        this.worldPosCallable = new EntityWorldPosCallable(locomotiveEntity);
        this.addSlots(locomotiveEntity.getEngine().getFuelHandler(), playerInventory);
    }

    public void addSlots(IItemHandler handler, PlayerInventory playerInventory) {
        this.addSlot(new FuelSlot(handler, 0, 11, 53));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 143));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.worldPosCallable.applyOrElse(WorldHelper.isPlayerNear(player)::test, true);
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

    public int getSteam() {
        Double steamAmount = this.steam.get();
        if (steamAmount != null) {
            return (int) Math.floor(steamAmount);
        } else {
            return 0;
        }
    }

    @Nonnull
    public FluidStack getWater() {
        return this.water.getOrElse(FluidStack.EMPTY);
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
            ItemStack slotItemStack = slot.getStack();
            itemstack = slotItemStack.copy();
            int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!this.mergeItemStack(slotItemStack, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotItemStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItemStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotItemStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItemStack);
        }

        return itemstack;
    }

    public Property<Boolean> getOn() {
        return this.on;
    }


    public Property<Integer> getSpeed() {
        return this.speed;
    }
}
