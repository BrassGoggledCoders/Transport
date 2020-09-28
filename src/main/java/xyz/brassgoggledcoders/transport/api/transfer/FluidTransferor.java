package xyz.brassgoggledcoders.transport.api.transfer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidTransferor implements ITransferor<IFluidHandler> {
    @Override
    public Capability<IFluidHandler> getCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public boolean transfer(IFluidHandler from, IFluidHandler to, int transferAmount) {
        FluidStack output = from.drain(5000, IFluidHandler.FluidAction.SIMULATE);
        if (!output.isEmpty()) {
            int filledAmount = to.fill(output, IFluidHandler.FluidAction.SIMULATE);
            if (filledAmount > 0) {
                return to.fill(from.drain(filledAmount, IFluidHandler.FluidAction.EXECUTE),
                        IFluidHandler.FluidAction.EXECUTE) > 0;
            }
        }
        return false;
    }

    @Override
    public int getDefaultAmount() {
        return 5 * FluidAttributes.BUCKET_VOLUME;
    }
}
