package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class FluidStorageShellContent extends ShellContent implements INBTSerializable<CompoundTag> {
    private final FluidTank fluidTank;
    private final BlockState blockState;
    private final LazyOptional<FluidTank> lazyOptional;

    public FluidStorageShellContent(BlockState blockState, int capacity) {
        this.blockState = blockState;
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
    public BlockState getView() {
        return blockState;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("fluid", fluidTank.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        fluidTank.readFromNBT(nbt.getCompound("fluid"));
    }
}
