package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.capability.itemhandler.FuelItemStackHandler;

import java.util.function.BooleanSupplier;

public class SteamEngine extends Engine {
    public static final int WATER_CAPACITY = 7 * FluidAttributes.BUCKET_VOLUME;

    private final FluidTank waterTank = new FluidTank(
            WATER_CAPACITY,
            fluidStack -> fluidStack.getFluid().isIn(FluidTags.WATER)
    );
    private final FuelItemStackHandler fuelHandler = new FuelItemStackHandler(1);
    private final BooleanSupplier isOn;

    private int burnRemaining;
    private int maxBurn;
    private double steam;

    private int ticksToNextWater;

    public SteamEngine(BooleanSupplier isOn) {
        this.isOn = isOn;
    }

    @Override
    public void tick() {
        if (this.isOn.getAsBoolean()) {
            if (burnRemaining > 0) {
                burnRemaining--;
                if (ticksToNextWater > 0) {
                    ticksToNextWater--;
                    steam += 1;
                } else {
                    if (!this.waterTank.drain(1, FluidAction.EXECUTE).isEmpty()) {
                        ticksToNextWater = 20;
                        steam += 1;
                    }
                }


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
    public boolean pullPower(EngineState engineState) {
        if (engineState == EngineState.NEUTRAL_0) {
            if (steam > 500) {
                steam -= engineState.getFuelUseModifier();
            }
            return true;
        } else {
            if (steam > 500) {
                steam -= engineState.getFuelUseModifier();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("fuelHandler", fuelHandler.serializeNBT());
        nbt.putInt("burnRemaining", this.burnRemaining);
        nbt.putInt("maxBurn", this.maxBurn);
        nbt.put("waterTank", this.waterTank.writeToNBT(new CompoundNBT()));
        nbt.putDouble("steam", this.steam);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.fuelHandler.deserializeNBT(nbt.getCompound("fuelHandler"));
        this.burnRemaining = nbt.getInt("burnRemaining");
        this.maxBurn = nbt.getInt("maxBurn");
        this.waterTank.readFromNBT(nbt.getCompound("waterTank"));
        this.steam = nbt.getDouble("steam");
    }
}
