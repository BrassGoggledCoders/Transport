package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

public class BlockEntityFluidStorage extends BlockEntityCapabilityStorage<IFluidHandler, FluidTank> {
    public BlockEntityFluidStorage(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public BlockEntityFluidStorage(BlockPos pWorldPos, BlockState pBlockState) {
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
        return new FluidTank(FluidAttributes.BUCKET_VOLUME * 64);
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
}
