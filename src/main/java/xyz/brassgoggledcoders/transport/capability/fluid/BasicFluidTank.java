package xyz.brassgoggledcoders.transport.capability.fluid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class BasicFluidTank extends FluidTank implements INBTSerializable<CompoundNBT> {

    public BasicFluidTank(int capacity) {
        super(capacity);
    }

    public BasicFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    public int getComparatorLevel() {
        return (int) (15F * ((float) this.getFluidAmount() / (float) this.getCapacity()));
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.writeToNBT(new CompoundNBT());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.readFromNBT(nbt);
    }
}
