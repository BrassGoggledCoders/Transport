package xyz.brassgoggledcoders.transport.cargoinstance.capabilitycargoinstance;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.capability.FluidTankPlusComponent;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;

import java.util.Collections;
import java.util.List;

public class FluidCargoInstance extends CapabilityCargoInstance<IFluidHandler> {
    private final FluidTankPlusComponent<?> fluidTank;
    private final LazyOptional<IFluidHandler> lazyFluidTank;

    public FluidCargoInstance(Cargo cargo) {
        super(cargo, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        this.fluidTank = new FluidTankPlusComponent<>("Tank", 10000, 80, 28);
        this.lazyFluidTank = LazyOptional.of(() -> fluidTank);
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
    public List<IContainerAddon> getContainerAddons() {
        return Collections.singletonList(fluidTank);
    }
}
