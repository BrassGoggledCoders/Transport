package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class FluidStorageShellContent extends ShellContent {
    private final FluidTank fluidTank;
    private final LazyOptional<FluidTank> lazyOptional;
    private final boolean allowItemInteraction;

    public FluidStorageShellContent(int capacity, boolean allowItemInteraction) {
        this.fluidTank = new FluidTank(capacity);
        this.lazyOptional = LazyOptional.of(() -> this.fluidTank);
        this.allowItemInteraction = allowItemInteraction;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
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

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (allowItemInteraction && FluidUtil.interactWithFluidHandler(pPlayer, pHand, this.fluidTank)) {
            return InteractionResult.sidedSuccess(this.getLevel().isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }
}
