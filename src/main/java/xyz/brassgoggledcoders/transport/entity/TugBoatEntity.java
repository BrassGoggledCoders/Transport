package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class TugBoatEntity extends BoatEntity {
    public TugBoatEntity(EntityType<? extends BoatEntity> type, World world) {
        super(type, world);
    }

    public TugBoatEntity(EntityType<? extends TugBoatEntity> entityType, World world, double x, double y, double z) {
        this(entityType, world);
        this.setPosition(x, y, z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
