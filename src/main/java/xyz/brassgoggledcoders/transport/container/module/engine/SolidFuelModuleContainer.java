package xyz.brassgoggledcoders.transport.container.module.engine;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.container.BasicContainer;
import xyz.brassgoggledcoders.transport.container.slot.FuelSlot;
import xyz.brassgoggledcoders.transport.network.property.IPropertyManaged;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyTypes;

import java.util.function.IntSupplier;

public class SolidFuelModuleContainer extends BasicContainer implements IPropertyManaged {
    private final Property<Integer> burnTimeRemaining;
    private final Property<Integer> maxBurnTime;

    public SolidFuelModuleContainer(
            ContainerType<?> type,
            int id,
            PlayerInventory playerInventory
    ) {
        this(
                type,
                id,
                playerInventory,
                IWorldPosCallable.DUMMY,
                new ItemStackHandler(1),
                PropertyTypes.INTEGER.create(),
                PropertyTypes.INTEGER.create()
        );
    }

    public SolidFuelModuleContainer(
            ContainerType<SolidFuelModuleContainer> type,
            int id,
            PlayerInventory playerInventory,
            IWorldPosCallable worldPosCallable,
            IItemHandler fuelHandler,
            IntSupplier burnTimeRemaining,
            IntSupplier maxBurnTime
    ) {
        this(
                type,
                id,
                playerInventory,
                worldPosCallable,
                fuelHandler,
                PropertyTypes.INTEGER.create(burnTimeRemaining::getAsInt),
                PropertyTypes.INTEGER.create(maxBurnTime::getAsInt)
        );
    }

    public SolidFuelModuleContainer(
            ContainerType<?> type,
            int id,
            PlayerInventory playerInventory,
            IWorldPosCallable worldPosCallable,
            IItemHandler fuelHandler,
            Property<Integer> burnTimeRemaining,
            Property<Integer> maxBurnTime
    ) {
        super(type, id, worldPosCallable);
        this.burnTimeRemaining = this.getPropertyManager()
                .addTrackedProperty(burnTimeRemaining);
        this.maxBurnTime = this.getPropertyManager()
                .addTrackedProperty(maxBurnTime);

        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
        this.addSlot(new FuelSlot(fuelHandler, 0, 80, 41));
    }

    public int getFuelBurnedScaled() {
        Integer maxBurnValue = this.maxBurnTime.get();
        Integer burnRemainingValue = this.burnTimeRemaining.get();
        if (maxBurnValue != null && burnRemainingValue != null && maxBurnValue > 0) {
            float percentBurned = (float) burnRemainingValue / maxBurnValue;
            return (int) Math.ceil(percentBurned * 14);
        } else {
            return 0;
        }
    }
}