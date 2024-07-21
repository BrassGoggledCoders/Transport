package xyz.brassgoggledcoders.transport.blockentity.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

public class EnergyStorageBlockEntity extends CapabilityStorageBlockEntity<IEnergyStorage, EnergyStorage> {
    public EnergyStorageBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public EnergyStorageBlockEntity(BlockPos pWorldPos, BlockState pBlockState) {
        this(
                TransportBlocks.ENERGY_STORAGE
                        .<BlockEntityType<?>, BlockEntityType<?>>getSibling(Registries.BLOCK_ENTITY_TYPE)
                        .get(),
                pWorldPos,
                pBlockState
        );
    }

    @Override
    public EntityCapability<IEnergyStorage, Direction> getCapability() {
        return Capabilities.EnergyStorage.ENTITY;
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
        this.getStorage().deserializeNBT(compoundTag.getCompound("energy"));
    }
}
