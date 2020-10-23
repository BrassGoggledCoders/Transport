package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class LocomotiveEntity extends AbstractMinecartEntity {
    public LocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world) {
        super(type, world);
    }

    public LocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        return Type.FURNACE;
    }
}
