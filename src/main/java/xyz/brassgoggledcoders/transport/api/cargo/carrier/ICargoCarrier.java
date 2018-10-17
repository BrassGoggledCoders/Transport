package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public interface ICargoCarrier extends INBTSerializable<NBTTagCompound> {
    Optional<World> getWorld();

    ICargo getCargo();

    ICargoInstance getCargoInstance();

    boolean canPlayerInteractWith(EntityPlayer entityPlayer);
}
