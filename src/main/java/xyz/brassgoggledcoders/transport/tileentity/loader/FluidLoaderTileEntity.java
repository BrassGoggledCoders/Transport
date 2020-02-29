package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.transport.capability.FluidHandlerDirectional;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class FluidLoaderTileEntity extends BasicLoaderTileEntity<IFluidHandler> {
    private final FluidTankComponent<FluidLoaderTileEntity> fluidTankComponent;
    private final LazyOptional<IFluidHandler> lazyFluid;

    public FluidLoaderTileEntity() {
        super(TransportBlocks.FLUID_LOADER.getTileEntityType(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        this.fluidTankComponent = new FluidTankComponent<>("Loader", 10000, 50, 24);
        this.lazyFluid = LazyOptional.of(() -> fluidTankComponent);
    }

    @Override
    protected void transfer(IFluidHandler from, IFluidHandler to) {

    }

    @Override
    protected LazyOptional<IFluidHandler> getInternalCAP() {
        return lazyFluid;
    }

    @Override
    protected LazyOptional<IFluidHandler> createOutputCAP() {
        return LazyOptional.of(() -> new FluidHandlerDirectional(fluidTankComponent, false));
    }

    @Override
    protected LazyOptional<IFluidHandler> createInputCAP() {
        return LazyOptional.of(() -> new FluidHandlerDirectional(fluidTankComponent, true));
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return fluidTankComponent.getScreenAddons();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        fluidTankComponent.readFromNBT(nbt.getCompound("tank"));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("tank", fluidTankComponent.writeToNBT(new CompoundNBT()));
        return nbt;
    }
}
