package xyz.brassgoggledcoders.transport.container.module.engine;

import net.minecraft.inventory.container.IContainerListener;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.module.container.IModularContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;
import xyz.brassgoggledcoders.transport.container.slot.FuelSlot;
import xyz.brassgoggledcoders.transport.network.property.IPropertyManaged;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;
import xyz.brassgoggledcoders.transport.network.property.PropertyTypes;

import java.util.Collection;
import java.util.Collections;
import java.util.function.IntSupplier;

public class SolidFuelModuleContainer extends ModuleContainer implements IPropertyManaged {
    private final IItemHandler fuelHandler;

    private final PropertyManager propertyManager;

    private final Property<Integer> burnRemaining;
    private final Property<Integer> maxBurnTime;

    public SolidFuelModuleContainer(
            IModularContainer modularContainer,
            IItemHandler fuelHandler,
            IntSupplier burnRemaining,
            IntSupplier maxBurnTime
    ) {
        super(modularContainer);
        this.fuelHandler = fuelHandler;

        this.propertyManager = new PropertyManager(modularContainer.getId());
        if (modularContainer.getPlayerInventory().player.getEntityWorld().isRemote()) {
            this.burnRemaining = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
            this.maxBurnTime = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create());
        } else {
            this.burnRemaining = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(burnRemaining::getAsInt));
            this.maxBurnTime = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(maxBurnTime::getAsInt));
        }
    }

    @Override
    public void setup() {
        super.setup();
        this.getModularContainer().putSlot(new FuelSlot(fuelHandler, 0, 80, 41));
        this.addPlayerSlots();
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        this.getPropertyManager().sendChanges(Collections.singletonList(listener), true, true);
    }

    public int getFuelBurnedScaled() {
        Integer maxBurnValue = this.maxBurnTime.get();
        Integer burnRemainingValue = this.burnRemaining.get();
        if (maxBurnValue != null && burnRemainingValue != null && maxBurnValue > 0) {
            float percentBurned = (float) burnRemainingValue / maxBurnValue;
            return (int) Math.ceil(percentBurned * 14);
        } else {
            return 0;
        }
    }

    @Override
    public void detectAndSendChanges(Collection<IContainerListener> listenerList) {
        super.detectAndSendChanges(listenerList);
        this.getPropertyManager().sendChanges(listenerList, true, false);
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }
}