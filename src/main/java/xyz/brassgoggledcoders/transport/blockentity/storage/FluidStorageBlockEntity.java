package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.Objects;

public class FluidStorageBlockEntity extends CapabilityStorageBlockEntity<IFluidHandler, FluidTank> {
    public FluidStorageBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public FluidStorageBlockEntity(BlockPos pWorldPos, BlockState pBlockState) {
        this(
                TransportBlocks.FLUID_STORAGE
                        .getSibling(ForgeRegistries.BLOCK_ENTITIES)
                        .get(),
                pWorldPos,
                pBlockState
        );
    }

    @Override
    public Capability<IFluidHandler> getCapability() {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @NotNull
    @Override
    public FluidTank createStorage() {
        return new FluidTank(FluidAttributes.BUCKET_VOLUME * 50);
    }

    @Override
    public int getAnalogOutputSignal() {
        return (int) Math.floor(this.getStorage().getFluidAmount() / (double) this.getStorage().getCapacity());
    }

    @Override
    public CompoundTag saveStorage() {
        return this.getStorage().writeToNBT(new CompoundTag());
    }

    @Override
    public void loadStorage(CompoundTag compoundTag) {
        this.getStorage().readFromNBT(compoundTag);
    }

    @Override
    public InteractionResult use(Player pPlayer, InteractionHand pHand) {
        if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, this.getStorage())) {
            return InteractionResult.sidedSuccess(Objects.requireNonNull(this.level).isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }
}
