package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

public class EnergyStorageBlockEntity extends CapabilityStorageBlockEntity<IEnergyStorage, EnergyStorage> {
    public EnergyStorageBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public EnergyStorageBlockEntity(BlockPos pWorldPos, BlockState pBlockState) {
        this(
                TransportBlocks.ENERGY_STORAGE
                        .getSibling(ForgeRegistries.BLOCK_ENTITY_TYPES)
                        .get(),
                pWorldPos,
                pBlockState
        );
    }

    @Override
    public Capability<IEnergyStorage> getCapability() {
        return ForgeCapabilities.ENERGY;
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
