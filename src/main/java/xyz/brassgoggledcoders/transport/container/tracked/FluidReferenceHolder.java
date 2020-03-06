package xyz.brassgoggledcoders.transport.container.tracked;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.IIntArray;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import xyz.brassgoggledcoders.transport.capability.FluidTankPlusComponent;

public class FluidReferenceHolder implements IIntArray {
    private final FluidTankPlusComponent<?> fluidTank;

    public FluidReferenceHolder(FluidTankPlusComponent<?> fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public int get(int index) {
        FluidStack fluidStack = this.fluidTank.getFluid();
        if (fluidStack.isEmpty() && index == 0) {
            return -1;
        } else if (index == 0) {
            return ((ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS).getID(fluidStack.getFluid());
        } else {
            return fluidStack.getAmount();
        }
    }

    @Override
    public void set(int index, int value) {
        if (index == 0 && value == -1) {
            this.fluidTank.setFluidStack(FluidStack.EMPTY);
        } else if (index == 0) {
            this.fluidTank.setFluidStack(new FluidStack(((ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS).getValue(value),
                    this.fluidTank.getFluidAmount()));
        } else {
            this.fluidTank.getFluid().setAmount(value);
        }
    }

    @Override
    public int size() {
        return 2;
    }
}
