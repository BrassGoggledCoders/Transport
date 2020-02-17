package xyz.brassgoggledcoders.transport.tileentity.loader;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidLoader extends TileEntityLoaderBase<IFluidHandler> {
    private FluidTank fluidTank;

    public TileEntityFluidLoader() {
        fluidTank = new FluidTank(8000);
    }

    @Override
    protected void readCapability(NBTTagCompound data) {

    }

    @Override
    protected void writeCapability(NBTTagCompound data) {

    }

    @Override
    protected Capability<IFluidHandler> getCapabilityType() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    protected IFluidHandler getInternalCapability() {
        return fluidTank;
    }

    @Override
    protected IFluidHandler getOutputCapability() {
        return fluidTank;
    }

    @Override
    protected IFluidHandler getInputCapability() {
        return fluidTank;
    }

    @Override
    protected boolean transfer(IFluidHandler from, IFluidHandler to) {
        return false;
    }
}
