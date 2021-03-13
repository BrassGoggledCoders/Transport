package xyz.brassgoggledcoders.transport.container.loader;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.container.BasicContainer;
import xyz.brassgoggledcoders.transport.network.property.Property;
import xyz.brassgoggledcoders.transport.network.property.PropertyTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class FluidLoaderContainer extends BasicContainer {
    private final Property<FluidStack> fluidStack;
    private final Property<Integer> tankSize;

    public FluidLoaderContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                                IWorldPosCallable worldPosCallable, Supplier<FluidStack> fluidStackSupplier, int tankSize) {
        super(type, id, worldPosCallable);
        this.fluidStack = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.FLUID_STACK.create(fluidStackSupplier));
        this.tankSize = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.INTEGER.create(() -> tankSize));

        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    public FluidLoaderContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory) {
        super(type, id, IWorldPosCallable.DUMMY);
        this.fluidStack = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.FLUID_STACK.create());
        this.tankSize = this.getPropertyManager()
                .addTrackedProperty(PropertyTypes.INTEGER.create());

        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    @Nonnull
    public FluidStack getFluidStack() {
        return fluidStack.getOrElse(FluidStack.EMPTY);
    }

    public int getTankSize() {
        return tankSize.getOrElse(1000);
    }
}
