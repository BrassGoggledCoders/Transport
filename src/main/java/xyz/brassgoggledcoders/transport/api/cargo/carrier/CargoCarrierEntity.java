package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.lang.ref.WeakReference;
import java.util.Optional;

public class CargoCarrierEntity implements ICargoCarrier, INBTSerializable<NBTTagCompound> {
    private final WeakReference<Entity> entity;
    private ICargo cargo;
    private ICargoInstance cargoInstance;

    public CargoCarrierEntity(Entity entity, ICargo cargo) {
        this.entity = new WeakReference<>(entity);
        this.cargo = cargo;
        this.cargoInstance = cargo.create(entity.getEntityWorld());
    }

    @Override
    public Optional<World> getWorld() {
        return Optional.ofNullable(entity.get())
                .map(Entity::getEntityWorld);
    }

    @Override
    public ICargo getCargo() {
        return cargo;
    }

    @Override
    public ICargoInstance getCargoInstance() {
        return cargoInstance;
    }

    @Override
    public boolean canPlayerInteractWith(EntityPlayer entityPlayer) {
        return Optional.ofNullable(entity.get())
                .filter(Entity::isEntityAlive)
                .filter(entity -> entityPlayer.getDistance(entity) < 64D)
                .isPresent();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound cargoTag = new NBTTagCompound();
        cargoTag.setString("name", cargo.getRegistryName().toString());
        cargoTag.setTag("instance", cargoInstance.writeToNBT());
        return cargoTag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ICargo cargo = TransportAPI.getCargoRegistry().getEntry(new ResourceLocation(nbt.getString("name")));
        ICargoInstance cargoInstance = cargo.create(this.getWorld().orElse(null));
        cargoInstance.readFromNBT(nbt.getCompoundTag("instance"));
    }
}
