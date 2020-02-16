package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public interface ICargoCarrier extends INBTSerializable<CompoundNBT> {
    Optional<World> getWorld();

    Cargo getCargo();

    ICargoInstance getCargoInstance();

    boolean canInteractWith(PlayerEntity playerEntity);
}
