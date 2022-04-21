package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.Objects;

public class EnergyStorageBlockEntity extends CapabilityStorageBlockEntity<IEnergyStorage, EnergyStorage> {
    public EnergyStorageBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public EnergyStorageBlockEntity(BlockPos pWorldPos, BlockState pBlockState) {
        this(
                TransportBlocks.ENERGY_STORAGE
                        .getSibling(ForgeRegistries.BLOCK_ENTITIES)
                        .get(),
                pWorldPos,
                pBlockState
        );
    }

    @Override
    public Capability<IEnergyStorage> getCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @NotNull
    @Override
    public EnergyStorage createStorage() {
        return new EnergyStorage(50000);
    }

    @Override
    public int getAnalogOutputSignal() {
        return (int) Math.floor(this.getStorage().getEnergyStored() / (double) this.getStorage().getMaxEnergyStored());
    }

    @Override
    public CompoundTag saveStorage() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("energy", this.getStorage().serializeNBT());
        return compoundTag;
    }

    @Override
    public void loadStorage(CompoundTag compoundTag) {
        this.getStorage().deserializeNBT(compoundTag.get("energy"));
    }
}
