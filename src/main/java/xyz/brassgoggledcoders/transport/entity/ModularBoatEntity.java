package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.*;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;
import xyz.brassgoggledcoders.transport.content.TransportModuleTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class ModularBoatEntity extends HulledBoatEntity implements IHoldable, IEntityAdditionalSpawnData, IItemProvider,
        IComparatorEntity {
    private final LazyOptional<IModularEntity> modularEntityLazy;
    private final ModularEntity<ModularBoatEntity> modularEntity;

    public ModularBoatEntity(EntityType<? extends ModularBoatEntity> type, World world) {
        super(type, world);
        this.modularEntity = new ModularEntity<>(this, TransportModuleSlots.CARGO);
        this.modularEntityLazy = LazyOptional.of(() -> this.modularEntity);
    }

    public ModularBoatEntity(World world, double x, double y, double z) {
        this(TransportEntities.MODULAR_BOAT.get(), world);
        this.setPosition(x, y, z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    @Override
    public void tick() {
        super.tick();
        modularEntity.getModuleInstances().forEach(ModuleInstance::tick);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TransportAPI.MODULAR_ENTITY) {
            return modularEntityLazy.cast();
        }

        if (side == Direction.DOWN || this.getHorizontalFacing().getOpposite() == side) {
            List<LazyOptional<T>> preferredCapabilities = modularEntity.getCapabilities(cap, side,
                    TransportModuleSlots.BACK.get());
            for (LazyOptional<T> lazyOptional : preferredCapabilities) {
                if (lazyOptional.isPresent()) {
                    return lazyOptional;
                }
            }
        }

        LazyOptional<T> capability = this.modularEntity.getCapability(cap, side);
        if (capability.isPresent()) {
            return capability;
        }

        return super.getCapability(cap, side);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        CargoModuleInstance cargoModuleInstance = this.getModularEntity().getModuleInstance(TransportModuleTypes.CARGO);
        if (cargoModuleInstance != null) {
            ActionResultType actionResultType = cargoModuleInstance.applyInteraction(player, vec, hand);
            if (actionResultType != ActionResultType.PASS) {
                return actionResultType;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    protected boolean canFitPassenger(@Nonnull Entity passenger) {
        return this.modularEntity.getModuleInstance(TransportModuleSlots.CARGO.get()) == null &&
                super.canFitPassenger(passenger);
    }

    @Override
    public boolean showPaddles() {
        return this.getModularEntity().getModuleInstance(TransportModuleSlots.CARGO.get()) == null;
    }

    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.world.isRemote && this.isAlive()) {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
            this.markVelocityChanged();
            boolean flag = source.getTrueSource() instanceof PlayerEntity && ((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode;
            if (flag || this.getDamageTaken() > 40.0F) {
                if (!flag && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                    ItemStack itemStack = this.modularEntity.asItemStack();

                    if (this.hasCustomName()) {
                        itemStack.setDisplayName(this.getCustomName());
                    }
                    CompoundNBT tagNBT = itemStack.getTag();
                    if (tagNBT == null) {
                        tagNBT = new CompoundNBT();
                    }
                    tagNBT.putString("hull_type", Objects.requireNonNull(this.getHullType().getRegistryName()).toString());
                    itemStack.setTag(tagNBT);

                    this.entityDropItem(itemStack);
                }

                this.remove();
            }

            return true;
        } else {
            return true;
        }
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.modularEntity.deserializeNBT(compound.getCompound("modules"));
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("modules", this.modularEntity.serializeNBT());
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer spawnData) {
        super.writeSpawnData(spawnData);
        this.modularEntity.write(spawnData);
    }

    @Override
    public void readSpawnData(PacketBuffer spawnData) {
        super.readSpawnData(spawnData);
        this.modularEntity.read(spawnData);
    }

    @Override
    public void onHeld() {
        for (ModuleInstance<?> moduleInstance : this.modularEntity.getModuleInstances()) {
            if (moduleInstance instanceof IHoldable) {
                ((IHoldable) moduleInstance).onHeld();
            }
        }
    }

    @Override
    public void onRelease() {
        for (ModuleInstance<?> moduleInstance : this.modularEntity.getModuleInstances()) {
            if (moduleInstance instanceof IHoldable) {
                ((IHoldable) moduleInstance).onRelease();
            }
        }
    }

    @Override
    public void push(float xPush, float zPush) {
        for (ModuleInstance<?> moduleInstance : this.modularEntity.getModuleInstances()) {
            if (moduleInstance instanceof IHoldable) {
                ((IHoldable) moduleInstance).push(xPush, zPush);
            }
        }
    }

    @Override
    @Nonnull
    public Item asItem() {
        return TransportEntities.MODULAR_BOAT_ITEM.get();
    }

    @Override
    public int getComparatorLevel() {
        CargoModuleInstance cargoModuleInstance = this.modularEntity.getModuleInstance(TransportObjects.CARGO_TYPE);
        return cargoModuleInstance != null ? cargoModuleInstance.getComparatorLevel() : -1;
    }

    public ModularEntity<ModularBoatEntity> getModularEntity() {
        return modularEntity;
    }
}
