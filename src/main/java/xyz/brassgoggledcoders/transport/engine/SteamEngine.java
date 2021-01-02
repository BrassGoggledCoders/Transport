package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.capability.itemhandler.FuelItemStackHandler;

import java.util.function.BooleanSupplier;

public class SteamEngine implements INBTSerializable<CompoundNBT> {
    public static final int WATER_CAPACITY = 7 * FluidAttributes.BUCKET_VOLUME;

    private final FluidTank waterTank = new FluidTank(
            WATER_CAPACITY,
            fluidStack -> fluidStack.getFluid().isIn(FluidTags.WATER)
    );

    private final FuelItemStackHandler fuelHandler = new FuelItemStackHandler(1);

    private final BooleanSupplier isOn;

    private int burnRemaining;
    private int maxBurn;

    public SteamEngine(BooleanSupplier isOn) {
        this.isOn = isOn;
    }

    public void tick() {
        if (this.isOn.getAsBoolean()) {
            if (burnRemaining > 0) {
                burnRemaining--;
            } else {
                this.maxBurn = this.fuelHandler.burnFuel();
                this.burnRemaining = this.maxBurn;
            }
        }
    }

    public IItemHandler getFuelHandler() {
        return fuelHandler;
    }

    public FluidTank getWaterTank() {
        return this.waterTank;
    }

    public int getBurnRemaining() {
        return this.burnRemaining;
    }

    public int getMaxBurn() {
        return this.maxBurn;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("fuelHandler", fuelHandler.serializeNBT());
        nbt.putInt("burnRemaining", this.burnRemaining);
        nbt.putInt("maxBurn", this.maxBurn);
        nbt.put("waterTank", this.waterTank.writeToNBT(new CompoundNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.fuelHandler.deserializeNBT(nbt.getCompound("fuelHandler"));
        this.burnRemaining = nbt.getInt("burnRemaining");
        this.maxBurn = nbt.getInt("maxBurn");
        this.waterTank.readFromNBT(nbt.getCompound("waterTank"));
    }
}
