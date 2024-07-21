package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.Objects;

public class FluidStorageBlockEntity extends CapabilityStorageBlockEntity<IFluidHandler, FluidTank> {


    public FluidStorageBlockEntity(BlockPos pWorldPos, BlockState pBlockState) {
        super(
                TransportBlocks.FLUID_STORAGE
                        .<BlockEntityType<?>, BlockEntityType<?>>getSibling(Registries.BLOCK_ENTITY_TYPE)
                        .get(),
                pWorldPos,
                pBlockState
        );
    }

    @Override
    public EntityCapability<IFluidHandler, Direction> getCapability() {
        return Capabilities.FluidHandler.ENTITY;
    }

    @NotNull
    @Override
    public FluidTank createStorage() {
        return new FluidTank(FluidType.BUCKET_VOLUME * 50);
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
