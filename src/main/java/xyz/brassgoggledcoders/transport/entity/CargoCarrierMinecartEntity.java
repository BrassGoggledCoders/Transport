package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargocarrier.CargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

import javax.annotation.Nonnull;

public class CargoCarrierMinecartEntity extends AbstractMinecartEntity {
    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world) {
        super(entityType, world);
    }

    public CargoCarrierMinecartEntity(World world, Cargo cargo) {
        this(TransportEntities.CARGO_MINECART.get(), world, cargo);
    }

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world, Cargo cargo) {
        super(entityType, world);
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        Transport.LOGGER.warn("Minecart Type called for Custom Minecart");
        return Type.CHEST;
    }
}
