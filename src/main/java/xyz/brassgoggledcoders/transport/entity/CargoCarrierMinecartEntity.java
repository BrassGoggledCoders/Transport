package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.CargoInstance;
import xyz.brassgoggledcoders.transport.api.engine.EngineInstance;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleCase;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CargoCarrierMinecartEntity extends AbstractMinecartEntity implements IModularEntity, IEntityAdditionalSpawnData {
    private final ModuleCase moduleCase;

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world) {
        super(entityType, world);
        this.moduleCase = new ModuleCase(this);
    }

    public CargoCarrierMinecartEntity(World world, double x, double y, double z) {
        this(TransportEntities.CARGO_MINECART.get(), world, x, y, z);
    }

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world,
                                      double x, double y, double z) {
        super(entityType, world, x, y, z);
        this.moduleCase = new ModuleCase(this);
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
        EngineInstance engineInstance = this.getModuleInstance(TransportObjects.ENGINE_TYPE);
        if (engineInstance != null && vec.getY() < 0.5D) {
            ActionResultType engineResult = engineInstance.applyInteraction(player, vec, hand);
            if (engineResult != ActionResultType.PASS) {
                return engineResult;
            }
        }
        CargoInstance cargoInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        if (cargoInstance != null) {
            ActionResultType cargoResult = cargoInstance.applyInteraction(player, vec, hand);
            if (cargoResult != ActionResultType.PASS) {
                return cargoResult;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    @Nonnull
    public BlockState getDisplayTile() {
        CargoInstance cargoInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        return cargoInstance != null ? cargoInstance.getBlockState() : super.getDisplayTile();
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("modules")) {
            this.moduleCase.deserializeNBT(compound.getCompound("modules"));
        } else if (compound.contains("cargo")) {
            CompoundNBT cargoNBT = compound.getCompound("cargo");
            if (cargoNBT.contains("name")) {
                Cargo cargo = TransportAPI.getCargo(cargoNBT.getString("name"));
                if (cargo != null) {
                    this.getModuleCase().addComponent(cargo);
                    if (cargoNBT.contains("instance")) {
                        for (CargoInstance cargoInstance : this.<Cargo, CargoInstance>getModuleInstances(TransportObjects.CARGO_TYPE)) {
                            if (cargoInstance.getModule() == cargo) {
                                cargoInstance.deserializeNBT(cargoNBT.getCompound("instance"));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("modules", this.getModuleCase().serializeNBT());
    }

    @Override
    public ItemStack getCartItem() {
        return this.moduleCase.createItemStack();
    }

    @Override
    @Nonnull
    public World getTheWorld() {
        return this.world;
    }

    @Nonnull
    @Override
    public Entity getSelf() {
        return this;
    }

    @Override
    public boolean canEquipComponent(Module<?> module) {
        return this.getModuleInstances(module.getType()).isEmpty();
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
    @Nonnull
    public ITextComponent getCarrierDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public ModuleCase getModuleCase() {
        return this.moduleCase;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        CargoInstance cargoInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        if (cargoInstance != null) {
            LazyOptional<T> lazyOptional = cargoInstance.getCapability(cap, side);
            if (lazyOptional.isPresent()) {
                return lazyOptional;
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void killMinecart(DamageSource source) {
        this.remove();
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            ItemStack itemStack = this.getModuleCase().createItemStack();

            if (this.hasCustomName()) {
                itemStack.setDisplayName(this.getCustomName());
            }

            this.entityDropItem(itemStack);
        }
    }

    @Override
    public int getComparatorLevel() {
        CargoInstance cargoInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        return cargoInstance != null ? cargoInstance.getComparatorLevel() : -1;
    }

    @Override
    @Nonnull
    public Item asItem() {
        return TransportEntities.CARGO_MINECART_ITEM.get();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        this.getModuleCase().write(buffer);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.getModuleCase().read(additionalData);
    }
}
