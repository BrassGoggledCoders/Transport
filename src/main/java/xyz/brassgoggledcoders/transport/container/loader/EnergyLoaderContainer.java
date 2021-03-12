package xyz.brassgoggledcoders.transport.container.loader;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.container.BasicContainer;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyTypes;

import javax.annotation.Nullable;
import java.util.function.IntSupplier;

public class EnergyLoaderContainer extends BasicContainer {
    private final Property<Integer> energy;
    private final Property<Integer> tankSize;

    public EnergyLoaderContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                                 IWorldPosCallable worldPosCallable, IntSupplier energySupplier, int maxEnergy) {
        super(type, id, worldPosCallable);
        this.energy = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.INTEGER.create(energySupplier::getAsInt));
        this.tankSize = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.INTEGER.create(() -> maxEnergy));

        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    public EnergyLoaderContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        super(type, id, IWorldPosCallable.DUMMY);
        this.energy = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.INTEGER.create());
        this.tankSize = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.INTEGER.create());

        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    public int getEnergy() {
        return energy.getOrElse(0);
    }

    public int getMaxEnergy() {
        return tankSize.getOrElse(1000);
    }
}
