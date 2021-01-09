package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.engine.Engine;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class LocomotiveEntity<T extends Engine> extends AbstractMinecartEntity {
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(LocomotiveEntity.class,
            DataSerializers.BOOLEAN);

    private float fuel;

    private double previousPushX;
    private double previousPushZ;
    private double pushX;
    private double pushZ;

    private final T engine;
    private boolean on;

    private EngineState previousMovingEngineState = EngineState.FORWARD_1;
    private EngineState engineState = EngineState.NEUTRAL_0;

    public LocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world) {
        super(type, world);
        this.engine = this.createEngine();
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @Nonnull
    public AbstractMinecartEntity.Type getMinecartType() {
        return AbstractMinecartEntity.Type.FURNACE;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(POWERED, true);
    }

    @Override
    public void tick() {
        this.engine.tick();
        if (!this.world.isRemote()) {
            if (this.on && this.engine.pullPower(this.engineState)) {
                if (this.pushX == 0 || this.pushZ == 0) {
                    this.pushX = previousPushX;
                    this.pushZ = previousPushZ;
                }
            } else {
                if (this.pushX != 0) {
                    this.previousPushX = this.pushX;
                    this.pushX = 0;
                }
                if (this.pushZ != 0) {
                    this.previousPushZ = this.pushZ;
                    this.pushZ = 0;
                }
            }
        }
        super.tick();
    }

    @Override
    protected double getMaximumSpeed() {
        return 0.4D;
    }

    @Override
    public double getMaxSpeedWithRail() {
        if (canUseRail()) {
            BlockPos pos = this.getCurrentRailPosition();
            BlockState state = getMinecart().world.getBlockState(pos);
            Block block = state.getBlock();
            if (state.isIn(BlockTags.RAILS) && block instanceof AbstractRailBlock) {
                float railMaxSpeed = ((AbstractRailBlock) block).getRailMaxSpeed(state, this.getEntityWorld(), pos, this);
                double maxSpeed = Math.min(railMaxSpeed, this.getMaximumSpeed() * this.engineState.getMaxSpeedModifier());
                return Math.min(maxSpeed, getCurrentCartSpeedCapOnRail());
            }
        }

        return getMaximumSpeed();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void moveAlongTrack(BlockPos pos, BlockState state) {
        super.moveAlongTrack(pos, state);
        Vector3d vector3d = this.getMotion();
        double horizontalMag = horizontalMag(vector3d);
        double d3 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d3 > 1.0E-4D && horizontalMag > 0.001D) {
            double d4 = MathHelper.sqrt(horizontalMag);
            double d5 = MathHelper.sqrt(d3);
            this.pushX = vector3d.x / d4 * d5;
            this.pushZ = vector3d.z / d4 * d5;
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
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        ActionResultType ret = super.processInitialInteract(player, hand);
        if (!ret.isSuccessOrConsume()) {
            if (player.getHeldItem(hand).getItem().isIn(TransportItemTags.WRENCHES)) {
                if (player.isCrouching()) {
                    if (this.increaseReverse()) {
                        return ActionResultType.func_233537_a_(this.world.isRemote);
                    } else {
                        return ActionResultType.FAIL;
                    }
                } else {
                    if (this.increaseForward()) {
                        return ActionResultType.func_233537_a_(this.world.isRemote);
                    } else {
                        return ActionResultType.FAIL;
                    }
                }
            }
            return ActionResultType.PASS;
        } else {
            return ret;
        }
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putFloat("fuel", this.fuel);

        compound.putDouble("previousPushX", this.previousPushX);
        compound.putDouble("previousPushZ", this.previousPushZ);
        compound.putDouble("pushX", this.pushX);
        compound.putDouble("pushZ", this.pushZ);

        compound.putBoolean("on", this.on);
        compound.putString("engineState", this.engineState.toString());
        compound.putString("previousMovingEngineState", this.previousMovingEngineState.toString());
        compound.put("engine", this.engine.serializeNBT());
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);

        this.fuel = compound.getFloat("fuel");

        this.previousPushX = compound.getDouble("previousPushX");
        this.previousPushZ = compound.getDouble("previousPushZ");
        this.pushX = compound.getDouble("pushX");
        this.pushZ = compound.getDouble("pushZ");

        this.on = compound.getBoolean("on");
        this.engineState = EngineState.byName(compound.getString("engineState"));
        this.previousMovingEngineState = EngineState.byName(compound.getString("previousMovingEngineState"));
        this.engine.deserializeNBT(compound.getCompound("engine"));
    }

    public boolean increaseForward() {
        EngineState forward = EngineState.forward(this.engineState);
        if (forward != null) {
            return alterEngineState(forward);
        } else {
            return false;
        }
    }

    public boolean increaseReverse() {
        EngineState reverse = EngineState.reverse(this.engineState);
        if (reverse != null) {
            return alterEngineState(reverse);
        } else {
            return false;
        }
    }

    public boolean alterEngineState(EngineState newEngineState) {
        if (newEngineState != this.engineState) {
            if (!newEngineState.getDirection().isMoving() && this.engineState.getDirection().isMoving()) {
                this.previousMovingEngineState = this.engineState;
                this.engineState = newEngineState;
                if (this.pushX != 0 || this.pushZ != 0) {
                    this.previousPushX = this.pushX;
                    this.previousPushZ = this.pushZ;
                    this.pushX = 0;
                    this.pushZ = 0;
                }
            } else if (newEngineState.getDirection() != this.previousMovingEngineState.getDirection()) {
                this.previousMovingEngineState = this.engineState;
                this.engineState = newEngineState;
                this.previousPushX *= -1;
                this.previousPushZ *= -1;
                if (pushX != 0 || pushZ != 0) {
                    this.pushX *= -1;
                    this.pushZ *= -1;
                }
            } else {
                this.previousMovingEngineState = this.engineState;
                this.engineState = newEngineState;
                this.pushX = previousPushX;
                this.pushZ = previousPushZ;
            }
            return true;
        }
        return false;
    }

    protected boolean isPowered() {
        return this.dataManager.get(POWERED);
    }

    protected void setPowered(boolean powered) {
        this.dataManager.set(POWERED, powered);
    }

    public void onPlaced(ItemUseContext context) {
        boolean positive = context.getPlacementHorizontalFacing().getAxisDirection() ==
                Direction.AxisDirection.POSITIVE;
        BlockState blockState = world.getBlockState(context.getPos());
        if (AbstractRailBlock.isRail(blockState)) {
            RailShape railShape = ((AbstractRailBlock) blockState.getBlock()).getRailDirection(blockState, world,
                    context.getPos(), this);
            switch (railShape) {
                case EAST_WEST:
                case ASCENDING_EAST:
                case ASCENDING_WEST:
                    this.rotationYaw = positive ? 0F : 180F;
                    break;
                case NORTH_SOUTH:
                case ASCENDING_NORTH:
                case ASCENDING_SOUTH:
                    this.rotationYaw = positive ? 90F : 270F;
                    break;
                default:
                    break;
            }
        }
        BlockPos pushPos = this.getPosition().offset(context.getPlacementHorizontalFacing().getOpposite(), 3);
        this.previousPushX = this.getPosX() - pushPos.getX();
        this.previousPushZ = this.getPosZ() - pushPos.getZ();
    }

    public EngineState getEngineState() {
        return engineState;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public T getEngine() {
        return this.engine;
    }

    public abstract T createEngine();
}
