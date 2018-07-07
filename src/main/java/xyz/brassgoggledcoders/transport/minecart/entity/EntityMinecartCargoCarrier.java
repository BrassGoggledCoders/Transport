package xyz.brassgoggledcoders.transport.minecart.entity;

import com.teamacronymcoders.base.entities.EntityMinecartBase;
import com.teamacronymcoders.base.entities.dataserializers.BaseDataSerializers;
import net.minecraft.item.ItemMinecart;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CargoCarrierEntity;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;
import xyz.brassgoggledcoders.transport.minecart.item.ItemMinecartCargoCarrier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityMinecartCargoCarrier extends EntityMinecartBase {
    @ObjectHolder(Transport.ID + "minecartCargoCarrier")
    private static ItemMinecartCargoCarrier itemMinecartCargoCarrier;

    private static final DataParameter<ResourceLocation> CARGO_REGISTRY_NAME =
            EntityDataManager.createKey(EntityMinecartCargoCarrier.class, BaseDataSerializers.RESOURCE_LOCATION);

    private ICargoCarrier cargoCarrier;

    public EntityMinecartCargoCarrier(World world) {
        super(world);
    }

    public EntityMinecartCargoCarrier(World world, ICargo cargo) {
        this(world);
        this.cargoCarrier = new CargoCarrierEntity(this, cargo);
        dataManager.set(CARGO_REGISTRY_NAME, cargo.getRegistryName());
    }

    @Override
    public void entityInit() {
        dataManager.register(CARGO_REGISTRY_NAME, new ResourceLocation(Transport.ID, "air"));
    }

    @Nonnull
    @Override
    public ItemMinecart getItem() {
        return itemMinecartCargoCarrier;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        return capability == CapabilityCargo.CARRIER;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        return capability == CapabilityCargo.CARRIER ? CapabilityCargo.CARRIER.cast(cargoCarrier) : null;
    }

    public ICargoCarrier getCargoCarrier() {
        return cargoCarrier;
    }
}
