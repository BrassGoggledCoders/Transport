package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;

import java.util.List;

public class FluidCargoModuleInstance extends CapabilityCargoModuleInstance<IFluidHandler> {
    private final FluidTankComponent<?> fluidTank;
    private final LazyOptional<IFluidHandler> lazyFluidTank;

    public FluidCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        this.fluidTank = new FluidTankComponent<>("Tank", 10000, 80, 28);
        this.lazyFluidTank = LazyOptional.of(() -> fluidTank);
    }

    @Override
    public ActionResultType applyInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
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

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return fluidTank.getScreenAddons();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return fluidTank.getContainerAddons();
    }
}
