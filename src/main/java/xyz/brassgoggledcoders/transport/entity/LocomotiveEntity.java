package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.block.AbstractRailBlock;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class LocomotiveEntity extends AbstractMinecartEntity {
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(LocomotiveEntity.class,
            DataSerializers.BOOLEAN);

    public int fuel;
    public double pushX;
    public double pushZ;

    public LocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world) {
        super(type, world);
    }

    public LocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world, double x, double y, double z) {
        super(type, world, x, y, z);
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
        super.tick();
    }

    @Override
    protected double getMaximumSpeed() {
        return 0.4D;
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
        if (ret.isSuccessOrConsume())
            return ret;

        this.pushX = this.getPosX() - player.getPosX();
        this.pushZ = this.getPosZ() - player.getPosZ();

        return ActionResultType.func_233537_a_(this.world.isRemote);
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("pushX", this.pushX);
        compound.putDouble("pushZ", this.pushZ);
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.pushX = compound.getDouble("pushX");
        this.pushZ = compound.getDouble("pushZ");
        this.fuel = compound.getShort("fuel");
    }

    protected boolean isMinecartPowered() {
        return this.dataManager.get(POWERED);
    }

    protected void setMinecartPowered(boolean powered) {
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

    }
}
