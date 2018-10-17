package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.CargoInstanceEmpty;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public class CargoCarrierEmpty implements ICargoCarrier {
    private CargoInstanceEmpty cargoInstanceEmpty = new CargoInstanceEmpty();
    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    public ICargo getCargo() {
        return TransportAPI.getCargoRegistry().getEmpty();
    }

    @Override
    public ICargoInstance getCargoInstance() {
        return cargoInstanceEmpty;
    }

    @Override
    public boolean canPlayerInteractWith(EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
