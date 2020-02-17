package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CargoCarrierMinecartEntity extends AbstractMinecartEntity {
    private static final DataParameter<ResourceLocation> CARGO_REGISTRY_NAME =
            EntityDataManager.createKey(CargoCarrierMinecartEntity.class, Transport.RESOURCE_LOCATION_DATA_SERIALIZER);

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world) {
        super(entityType, world);
    }

    public CargoCarrierMinecartEntity(World world, Cargo cargo) {
        this(TransportEntities.CARGO_MINECART.get(), world, cargo);
    }

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world, Cargo cargo) {
        super(entityType, world);
        dataManager.set(CARGO_REGISTRY_NAME, Optional.ofNullable(cargo.getRegistryName())
                .orElse(TransportAPI.EMPTY_CARGO_RL));
    }

    @Override
    public void registerData() {
        super.registerData();
        dataManager.register(CARGO_REGISTRY_NAME, TransportAPI.EMPTY_CARGO_RL);
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        Transport.LOGGER.warn("Minecart Type called for Custom Minecart");
        return Type.CHEST;
    }
}
