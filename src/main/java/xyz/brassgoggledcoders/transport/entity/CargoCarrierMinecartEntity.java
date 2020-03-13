package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.content.TransportCargoes;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.item.CargoCarrierMinecartItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class CargoCarrierMinecartEntity extends AbstractMinecartEntity implements ICargoCarrier {
    private static final DataParameter<ResourceLocation> CARGO_PARAMETER = EntityDataManager.createKey(
            CargoCarrierMinecartEntity.class, Transport.RESOURCE_LOCATION_DATA_SERIALIZER);

    private ITextComponent displayName;

    private Cargo cargo;
    private CargoInstance cargoInstance;

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world) {
        super(entityType, world);
    }

    public CargoCarrierMinecartEntity(World world, Cargo cargo, double x, double y, double z) {
        this(TransportEntities.CARGO_MINECART.get(), world, cargo, x, y, z);
    }

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world, Cargo cargo,
                                      double x, double y, double z) {
        super(entityType, world, x, y, z);
        this.cargo = cargo;
        this.getDataManager().set(CARGO_PARAMETER, Objects.requireNonNull(cargo.getRegistryName()));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(CARGO_PARAMETER, TransportCargoes.EMPTY.getId());
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        return Type.CHEST;
    }

    @Override
    @Nonnull
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        return this.getCargoInstance().applyInteraction(this, player, vec, hand);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        if (displayName == null) {
            displayName = new TranslationTextComponent("text.transport.with", super.getDisplayName(),
                    this.getCargoInstance().getDisplayName());
        }
        return displayName;
    }

    @Override
    @Nonnull
    public BlockState getDisplayTile() {
        return this.getCargoInstance().getBlockState();
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        CompoundNBT cargo = compound.getCompound("cargo");
        this.getDataManager().set(CARGO_PARAMETER, new ResourceLocation(cargo.getString("name")));
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        CompoundNBT cargo = new CompoundNBT();
        cargo.putString("name", this.getDataManager().get(CARGO_PARAMETER).toString());
        compound.put("cargo", cargo);
    }

    @Override
    public ItemStack getCartItem() {
        return CargoCarrierMinecartItem.getCartStack(this.getCargoInstance());
    }

    @Override
    public World getTheWorld() {
        return this.world;
    }

    @Nonnull
    @Override
    public Cargo getCargo() {
        if (cargo == null) {
            cargo = Optional.ofNullable(TransportAPI.CARGO.getValue(this.getDataManager().get(CARGO_PARAMETER)))
                    .orElseGet(TransportCargoes.EMPTY);
        }
        return cargo;
    }

    @Nonnull
    @Override
    public CargoInstance getCargoInstance() {
        if (cargoInstance == null) {
            cargoInstance = this.getCargo().create(this.getTheWorld());
        }
        return cargoInstance;
    }

    @Override
    public void openContainer(PlayerEntity playerEntity, INamedContainerProvider provider, Consumer<PacketBuffer> packetBufferConsumer) {
        if (playerEntity instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, provider, packetBuffer -> {
                packetBuffer.writeInt(this.getEntityId());
                packetBufferConsumer.accept(packetBuffer);
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return this.isAlive();
    }

    @Override
    public ITextComponent getCarrierDisplayName() {
        return super.getDisplayName();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> cargoLazyOptional = this.getCargoInstance().getCapability(cap, side);
        return cargoLazyOptional.isPresent() ? cargoLazyOptional : super.getCapability(cap, side);
    }

    @Override
    public void killMinecart(DamageSource source) {
        this.remove();
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            ItemStack itemStack = this.getCargo().createItemStack(TransportEntities.CARGO_MINECART_ITEM.get(),
                    this.getCargoInstance());

            if (this.hasCustomName()) {
                itemStack.setDisplayName(this.getCustomName());
            }

            this.entityDropItem(itemStack);
        }
    }
}
