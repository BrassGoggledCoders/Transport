package xyz.brassgoggledcoders.transport.shellcontent.storage.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class FluidStorageShellContent extends ShellContent {
    private final FluidTank fluidTank;
    private final boolean allowItemInteraction;

    public FluidStorageShellContent(int capacity, boolean allowItemInteraction) {
        this.fluidTank = new FluidTank(capacity);
        this.allowItemInteraction = allowItemInteraction;
    }

    public final IFluidHandler getHandler() {
        return this.fluidTank;
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
