package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModuleInstance;
import xyz.brassgoggledcoders.transport.api.engine.PoweredState;
import xyz.brassgoggledcoders.transport.api.entity.IHoldable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.entity.ModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class CargoCarrierMinecartEntity extends AbstractMinecartEntity implements IHoldable, IEntityAdditionalSpawnData,
        IItemProvider {
    private final LazyOptional<IModularEntity> modularEntityLazy;
    private final ModularEntity<CargoCarrierMinecartEntity> modularEntity;

    private double originalPushX;
    private double originalPushZ;

    private double pushX;
    private double pushZ;

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world) {
        super(entityType, world);
        this.modularEntity = new ModularEntity<>(this, TransportModuleSlots.CARGO, TransportModuleSlots.BACK);
        this.modularEntityLazy = LazyOptional.of(() -> this.modularEntity);
    }

    public CargoCarrierMinecartEntity(World world, double x, double y, double z) {
        this(TransportEntities.CARGO_MINECART.get(), world, x, y, z);
    }

    public CargoCarrierMinecartEntity(EntityType<CargoCarrierMinecartEntity> entityType, World world,
                                      double x, double y, double z) {
        super(entityType, world, x, y, z);
        this.modularEntity = new ModularEntity<>(this, TransportModuleSlots.CARGO, TransportModuleSlots.BACK);
        this.modularEntityLazy = LazyOptional.of(() -> this.modularEntity);
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
    @ParametersAreNonnullByDefault
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        EngineModuleInstance engineModuleInstance = this.modularEntity.getModuleInstance(TransportObjects.ENGINE_TYPE);
        if (engineModuleInstance != null) {
            if (originalPushX == 0D && originalPushZ == 0D) {
                originalPushX = this.getPosX() - player.getPosX();
                originalPushZ = this.getPosZ() - player.getPosZ();
            } else {
                ItemStack itemStack = player.getHeldItem(hand);
                if (!itemStack.isEmpty() && itemStack.getItem().isIn(TransportItemTags.WRENCHES)) {
                    originalPushX = -originalPushX;
                    originalPushZ = -originalPushZ;
                    this.setMotion(this.getMotion().mul(-1, 0, -1));
                    this.rotationYaw = this.rotationYaw - 180;
                    return ActionResultType.SUCCESS;
                }
            }
        }

        if (vec.y < 0.7) {
            switch (this.getHorizontalFacing()) {
                case NORTH:
                    if (vec.x <= -0.3) {
                        return modularEntity.applyPlayerInteraction(TransportModuleSlots.BACK, player, vec, hand);
                    }
                    break;
                case SOUTH:
                    if (vec.x >= 0.3) {
                        return modularEntity.applyPlayerInteraction(TransportModuleSlots.BACK, player, vec, hand);
                    }
                    break;
                case WEST:
                    if (vec.z >= 0.3) {
                        return modularEntity.applyPlayerInteraction(TransportModuleSlots.BACK, player, vec, hand);
                    }
                    break;
                case EAST:
                    if (vec.z <= -0.3) {
                        return modularEntity.applyPlayerInteraction(TransportModuleSlots.BACK, player, vec, hand);
                    }
                    break;
                default:
                    break;
            }
        }


        return modularEntity.applyPlayerInteraction(TransportModuleSlots.CARGO, player, vec, hand);
    }

    @Override
    public boolean isPoweredCart() {
        return this.modularEntity.getModuleInstance(TransportObjects.ENGINE_TYPE) != null;
    }

    @Override
    @Nonnull
    public BlockState getDisplayTile() {
        CargoModuleInstance cargoModuleInstance = this.modularEntity.getModuleInstance(TransportObjects.CARGO_TYPE);
        return cargoModuleInstance != null ? cargoModuleInstance.getBlockState() : super.getDisplayTile();
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.pushX = compound.getDouble("pushX");
        this.pushZ = compound.getDouble("pushZ");
        this.originalPushX = compound.getDouble("originalPushX");
        this.originalPushZ = compound.getDouble("originalPushZ");
        if (compound.contains("modules")) {
            this.modularEntity.deserializeNBT(compound.getCompound("modules"));
        } else if (compound.contains("cargo")) {
            CompoundNBT cargoNBT = compound.getCompound("cargo");
            if (cargoNBT.contains("name")) {
                CargoModule cargoModule = TransportAPI.getCargo(cargoNBT.getString("name"));
                if (cargoModule != null) {
                    ModuleInstance<CargoModule> moduleInstance = this.modularEntity.add(cargoModule,
                            TransportModuleSlots.CARGO.get(), false);
                    if (cargoNBT.contains("instance") && moduleInstance != null) {
                        moduleInstance.deserializeNBT(cargoNBT.getCompound("instance"));
                    }
                }
            }
        }
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("modules", this.modularEntity.serializeNBT());
        compound.putDouble("pushX", this.pushX);
        compound.putDouble("pushZ", this.pushZ);
        compound.putDouble("originalPushX", this.originalPushX);
        compound.putDouble("originalPushZ", this.originalPushZ);
    }

    @Override
    public ItemStack getCartItem() {
        return this.modularEntity.asItemStack();
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
    public void tick() {
        super.tick();
        modularEntity.getModuleInstances().forEach(ModuleInstance::tick);
        if (!this.world.isRemote()) {
            EngineModuleInstance engineModuleInstance = this.modularEntity.getModuleInstance(TransportObjects.ENGINE_TYPE);
            if (engineModuleInstance == null || engineModuleInstance.getPoweredState() != PoweredState.RUNNING ||
                    !engineModuleInstance.isRunning()) {
                if (pushX != 0.0D && pushZ != 0.0D) {
                    originalPushX = pushX;
                    originalPushZ = pushZ;
                }
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            } else if (pushX == 0.0D && pushZ == 0.0D) {
                this.pushX = originalPushX;
                this.pushZ = originalPushZ;
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void moveAlongTrack(BlockPos pos, BlockState state) {
        super.moveAlongTrack(pos, state);
        Vec3d vec3d = this.getMotion();
        double d2 = horizontalMag(vec3d);
        double d3 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d3 > 1.0E-4D && d2 > 0.001D) {
            double d4 = MathHelper.sqrt(d2);
            double d5 = MathHelper.sqrt(d3);
            this.pushX = vec3d.x / d4 * d5;
            this.pushZ = vec3d.z / d4 * d5;
        }
    }

    @Override
    protected void applyDrag() {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d0 > 1.0E-7D) {
            d0 = MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            this.setMotion(this.getMotion().mul(0.8D, 0.0D, 0.8D).add(this.pushX, 0.0D, this.pushZ));
        } else {
            this.setMotion(this.getMotion().mul(0.98D, 0.0D, 0.98D));
        }

        super.applyDrag();
    }

    @Override
    public void killMinecart(@Nonnull DamageSource source) {
        this.remove();
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            ItemStack itemStack = this.modularEntity.asItemStack();

            if (this.hasCustomName()) {
                itemStack.setDisplayName(this.getCustomName());
            }

            this.entityDropItem(itemStack);
        }
    }

    @Override
    public int getComparatorLevel() {
        CargoModuleInstance cargoModuleInstance = this.modularEntity.getModuleInstance(TransportObjects.CARGO_TYPE);
        return cargoModuleInstance != null ? cargoModuleInstance.getComparatorLevel() : -1;
    }

    @Override
    protected double getMaximumSpeed() {
        return this.modularEntity.<EngineModule, EngineModuleInstance, Double>callModule(TransportObjects.ENGINE_TYPE,
                EngineModuleInstance::getMaximumSpeed, () -> 0.4D);
    }

    @Override
    @Nonnull
    public Item asItem() {
        return TransportEntities.CARGO_MINECART_ITEM.get();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        this.modularEntity.write(buffer);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.modularEntity.read(additionalData);
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

    public void remove(boolean keepData) {
        super.remove(keepData);
        if (!keepData) {
            this.modularEntityLazy.invalidate();
            this.modularEntity.invalidateCapabilities();
        }
    }

    public void setOriginalPushes(PlayerEntity playerEntity) {
        this.setOriginalPushes(this.getPosX() - playerEntity.getPosX(), this.getPosZ() - playerEntity.getPosZ());
    }

    public void setOriginalPushes(double pushX, double pushZ) {
        this.originalPushX = pushX;
        this.originalPushZ = pushZ;
    }
}
