package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.entity.EntityWorldPosCallable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.container.loader.FluidLoaderContainer;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nullable;

public class FluidCargoModuleInstance extends CapabilityCargoModuleInstance<IFluidHandler> {
    private final FluidTankComponent<?> fluidTank;
    private final LazyOptional<IFluidHandler> lazyFluidTank;

    public FluidCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        this(cargoModule, modularEntity, 10);
    }

    public FluidCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity, int buckets) {
        super(cargoModule, modularEntity, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        this.fluidTank = new FluidTankComponent<>("Tank", buckets * FluidAttributes.BUCKET_VOLUME, 80, 28);
        this.lazyFluidTank = LazyOptional.of(() -> fluidTank);
    }

    @Override
    public ActionResultType applyInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if (FluidUtil.interactWithFluidHandler(player, hand, this.fluidTank)) {
            return ActionResultType.SUCCESS;
        }
        return super.applyInteraction(player, vec, hand);
    }

    @Override
    protected LazyOptional<IFluidHandler> getLazyOptional() {
        return lazyFluidTank;
    }

    @Override
    protected CompoundNBT serializeCapability() {
        return fluidTank.writeToNBT(new CompoundNBT());
    }

    @Override
    protected void deserializeCapability(CompoundNBT nbt) {
        fluidTank.readFromNBT(nbt);
    }

    @Nullable
    @Override
    public Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> getContainerCreator() {
        return (id, playerInventory, playerEntity) -> new FluidLoaderContainer(
                TransportContainers.FLUID_LOADER.get(),
                id,
                playerInventory,
                new EntityWorldPosCallable(this.getModularEntity()),
                this.fluidTank::getFluid,
                this.fluidTank.getTankCapacity(0)
        );
    }
}
