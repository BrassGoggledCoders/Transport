package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class FluidStorageShellContent extends ShellContent {
    private final FluidTank fluidTank;
    private final LazyOptional<FluidTank> lazyOptional;

    public FluidStorageShellContent(int capacity) {
        this.fluidTank = new FluidTank(capacity);
        this.lazyOptional = LazyOptional.of(() -> this.fluidTank);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return lazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyOptional.invalidate();
    }

    @Override
    @Nonnull
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.put("fluid", fluidTank.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(@Nonnull CompoundTag nbt) {
        super.deserializeNBT(nbt);
        fluidTank.readFromNBT(nbt.getCompound("fluid"));
    }
}
