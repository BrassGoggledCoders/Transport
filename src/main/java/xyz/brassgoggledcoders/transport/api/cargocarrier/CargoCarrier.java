package xyz.brassgoggledcoders.transport.api.cargocarrier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;

import java.util.Optional;

public class CargoCarrier implements ICargoCarrier, INBTSerializable<CompoundNBT> {
    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    public Cargo getCargo() {
        return null;
    }

    @Override
    public CargoInstance getCargoInstance() {
        return null;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
