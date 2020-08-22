package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportHullTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class HulledBoatEntity extends BoatEntity implements IEntityAdditionalSpawnData {
    private HullType hullType = TransportHullTypes.OAK.get();

    public HulledBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public HulledBoatEntity(@Nullable HullType hullType, World world, double x, double y, double z) {
        this(TransportEntities.HULLED_BOAT.get(), world);
        this.setHullType(hullType);
        this.setPosition(x, y, z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.setHullType(TransportAPI.HULL_TYPE.get().getValue(new ResourceLocation(compound.getString("hull_type"))));

    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putString("hull_type", Objects.requireNonNull(this.hullType.getRegistryName()).toString());
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeResourceLocation(Objects.requireNonNull(this.getHullType().getRegistryName()));
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.setHullType(TransportAPI.HULL_TYPE.get().getValue(additionalData.readResourceLocation()));
    }

    public void setHullType(@Nullable HullType hullType) {
        if (hullType != null) {
            this.hullType = hullType;
        } else {
            this.hullType = TransportHullTypes.OAK.get();
        }
    }

    public HullType getHullType() {
        return this.hullType;
    }

    public boolean showPaddles() {
        return true;
    }
}
