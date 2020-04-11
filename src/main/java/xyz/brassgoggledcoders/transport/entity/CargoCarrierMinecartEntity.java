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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModuleInstance;
import xyz.brassgoggledcoders.transport.api.engine.PoweredState;
import xyz.brassgoggledcoders.transport.api.entity.IHoldable;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleCase;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.content.TransportEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

public class CargoCarrierMinecartEntity extends AbstractMinecartEntity implements IModularEntity, IHoldable,
        IEntityAdditionalSpawnData {
    private final ModuleCase moduleCase;

    private double originalPushX;
    private double originalPushZ;

    private double pushX;
    private double pushZ;

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
    @ParametersAreNonnullByDefault
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        ItemStack playerHeldItem = player.getHeldItem(hand);
        if (!playerHeldItem.isEmpty()) {
            Module<?> module = TransportAPI.getModuleFromItem(playerHeldItem.getItem());
            if (module != null) {
                ModuleInstance<?> moduleInstance = this.moduleCase.addModule(module);
                if (moduleInstance != null) {
                    if (moduleInstance instanceof EngineModuleInstance) {
                        originalPushX = this.getPosX() - player.getPosX();
                        originalPushZ = this.getPosZ() - player.getPosZ();
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }

        EngineModuleInstance engineModuleInstance = this.getModuleInstance(TransportObjects.ENGINE_TYPE);
        if (engineModuleInstance != null && vec.getY() < 0.5D) {
            ActionResultType engineResult = engineModuleInstance.applyInteraction(player, vec, hand);
            if (engineResult != ActionResultType.PASS) {
                return engineResult;
            }
        }
        CargoModuleInstance cargoModuleInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        if (cargoModuleInstance != null) {
            ActionResultType cargoResult = cargoModuleInstance.applyInteraction(player, vec, hand);
            if (cargoResult != ActionResultType.PASS) {
                return cargoResult;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    @Nonnull
    public BlockState getDisplayTile() {
        CargoModuleInstance cargoModuleInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
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
            this.moduleCase.deserializeNBT(compound.getCompound("modules"));
        } else if (compound.contains("cargo")) {
            CompoundNBT cargoNBT = compound.getCompound("cargo");
            if (cargoNBT.contains("name")) {
                CargoModule cargoModule = TransportAPI.getCargo(cargoNBT.getString("name"));
                if (cargoModule != null) {
                    ModuleInstance<CargoModule> moduleInstance = this.getModuleCase().addModule(cargoModule);
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
        compound.put("modules", this.getModuleCase().serializeNBT());
        compound.putDouble("pushX", this.pushX);
        compound.putDouble("pushZ", this.pushZ);
        compound.putDouble("originalPushX", this.originalPushX);
        compound.putDouble("originalPushZ", this.originalPushZ);
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
        CargoModuleInstance cargoModuleInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        if (cargoModuleInstance != null) {
            LazyOptional<T> lazyOptional = cargoModuleInstance.getCapability(cap, side);
            if (lazyOptional.isPresent()) {
                return lazyOptional;
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        super.tick();
        moduleCase.getComponents().forEach(ModuleInstance::tick);
        if (!this.world.isRemote()) {
            EngineModuleInstance engineModuleInstance = this.getModuleInstance(TransportObjects.ENGINE_TYPE);
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
            ItemStack itemStack = this.getModuleCase().createItemStack();

            if (this.hasCustomName()) {
                itemStack.setDisplayName(this.getCustomName());
            }

            this.entityDropItem(itemStack);
        }
    }

    @Override
    public int getComparatorLevel() {
        CargoModuleInstance cargoModuleInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        return cargoModuleInstance != null ? cargoModuleInstance.getComparatorLevel() : -1;
    }

    @Override
    protected double getMaximumSpeed() {
        return this.<EngineModule, EngineModuleInstance, Double>callModule(TransportObjects.ENGINE_TYPE,
                EngineModuleInstance::getMaximumSpeed, () -> 0.4D);
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

    @Override
    public void onHeld() {
        for (ModuleInstance<?> moduleInstance: this.getModuleCase().getComponents()) {
            if (moduleInstance instanceof IHoldable) {
                ((IHoldable) moduleInstance).onHeld();
            }
        }
    }

    @Override
    public void onRelease() {
        for (ModuleInstance<?> moduleInstance: this.getModuleCase().getComponents()) {
            if (moduleInstance instanceof IHoldable) {
                ((IHoldable) moduleInstance).onRelease();
            }
        }
    }
}
