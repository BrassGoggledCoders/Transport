package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.capability.itemhandler.FuelItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public class SteamEngine extends Engine implements ICapabilityProvider {
    public static final int WATER_CAPACITY = 7 * FluidAttributes.BUCKET_VOLUME;

    private final FluidTank waterTank = new FluidTank(
            WATER_CAPACITY,
            fluidStack -> fluidStack.getFluid().isIn(FluidTags.WATER)
    );
    private final FuelItemStackHandler fuelHandler = new FuelItemStackHandler(1);
    private final BooleanSupplier isOn;

    private final LazyOptional<IFluidHandler> fluidLazy;
    private final LazyOptional<IItemHandler> itemLazy;

    private int burnRemaining;
    private int maxBurn;
    private double steam;

    private int ticksToNextWater;

    public SteamEngine(BooleanSupplier isOn) {
        this.isOn = isOn;
        this.fluidLazy = LazyOptional.of(() -> this.waterTank);
        this.itemLazy = LazyOptional.of(() -> this.fuelHandler);
    }

    @Override
    public void tick() {
        if (this.isOn.getAsBoolean()) {
            int burnAttempts = steam < 4100 ? 2 : 1;
            if (steam < 4200) {
                while (burnAttempts-- > 0) {
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
                        if (this.waterTank.getFluidAmount() > 0) {
                            this.maxBurn = this.fuelHandler.burnFuel();
                            this.burnRemaining = this.maxBurn;
                        }
                    }
                }
            }
        } else {
            if (burnRemaining > 0) {
                burnRemaining--;
            }
            if (steam > 0) {
                steam -= 0.4;
            }
        }
    }

    public IItemHandler getFuelHandler() {
        return fuelHandler;
    }

    public FluidTank getWaterTank() {
        return this.waterTank;
    }

    public double getSteam() {
        return this.steam;
    }

    public int getBurnRemaining() {
        return this.burnRemaining;
    }

    public int getMaxBurn() {
        return this.maxBurn;
    }

    @Override
    public boolean pullPower(EngineState engineState) {
        steam -= engineState.getFuelUseModifier();
        return steam > 2000;
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemLazy.cast();
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidLazy.cast();
        } else {
            return LazyOptional.empty();
        }
    }

    public void invalidateCaps() {
        this.fluidLazy.invalidate();
        this.itemLazy.invalidate();
    }
}
