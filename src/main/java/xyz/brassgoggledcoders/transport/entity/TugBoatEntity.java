package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CSteerBoatPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportCapabilities;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationNetwork;
import xyz.brassgoggledcoders.transport.api.navigation.INavigationPoint;
import xyz.brassgoggledcoders.transport.api.navigation.INavigator;
import xyz.brassgoggledcoders.transport.api.navigation.Navigator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TugBoatEntity extends BoatEntity {
    private static final DataParameter<Optional<BlockPos>> TARGET_POS = EntityDataManager.createKey(TugBoatEntity.class,
            DataSerializers.OPTIONAL_BLOCK_POS);

    private final INavigator navigator;
    private final LazyOptional<INavigator> navigatorLazy;

    private LazyOptional<INavigationNetwork> navigationNetwork;

    public TugBoatEntity(EntityType<? extends BoatEntity> type, World world) {
        super(type, world);
        this.navigator = new Navigator();
        this.navigatorLazy = LazyOptional.of(() -> this.navigator);
    }

    public TugBoatEntity(EntityType<? extends TugBoatEntity> entityType, World world, double x, double y, double z) {
        this(entityType, world);
        this.setPosition(x, y, z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET_POS, Optional.empty());
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f = 0.8F;
            float f1 = (float) ((!this.isAlive() ? (double) 0.01F : this.getMountedYOffset()) + passenger.getYOffset());

            Vector3d vector3d = new Vector3d(f, 0.0D, 0.0D)
                    .rotateYaw(-this.rotationYaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
            passenger.setPosition(this.getPosX() + vector3d.x, this.getPosY() + (double) f1,
                    this.getPosZ() + vector3d.z);
            passenger.rotationYaw += this.deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(passenger);
        }
    }

    @Override
    public double getMountedYOffset() {
        return 0D;
    }

    @Override
    public void tick() {
        INavigationNetwork navigationNetwork = this.getEntityWorld()
                .getCapability(TransportCapabilities.NAVIGATION_NETWORK)
                .resolve()
                .orElse(null);
        if (!this.navigator.getCurrentPoint(navigationNetwork).isPresent()) {
            this.navigator.setCurrentPoint(this.getEntityWorld()
                    .getCapability(TransportCapabilities.NAVIGATION_NETWORK)
                    .map(INavigationNetwork::getNavigationPoints)
                    .flatMap(collection -> collection.stream().findFirst())
                    .orElse(null));
        }
        Optional<INavigationPoint> currentPoint = this.navigator.getCurrentPoint(navigationNetwork);
        if (currentPoint.isPresent()) {
            if (this.getTargetPos().isPresent()) {
                BlockPos targetPos = this.getTargetPos().get();
                if (!targetPos.equals(currentPoint.get().getPosition())) {
                    this.setTargetPos(currentPoint.get().getPosition());
                }
            } else {
                this.setTargetPos(currentPoint.get().getPosition());
            }
        } else {
            this.setTargetPos(null);
        }
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        if (this.status != BoatEntity.Status.UNDER_WATER && this.status != BoatEntity.Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        } else {
            ++this.outOfControlTicks;
        }

        if (!this.world.isRemote && this.outOfControlTicks >= 60.0F) {
            this.removePassengers();
        }

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        this.entityTick();
        this.tickLerp();
        if (this.canPassengerSteer()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof PlayerEntity)) {
                this.setPaddleState(false, false);
            }

            this.updateMotion();
            if (this.world.isRemote) {
                this.controlBoat();
                this.world.sendPacketToServer(new CSteerBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }

            this.move(MoverType.SELF, this.getMotion());
        } else if (this.getTargetPos().isPresent()) {
            this.updateMotion();
            this.moveTowardsTarget();
            this.move(MoverType.SELF, this.getMotion());
        } else {
            this.setMotion(Vector3d.ZERO);
        }

        this.updateRocking();

        this.doBlockCollisions();
    }

    public void entityTick() {
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.baseTick();
    }

    public boolean canPassengerSteer() {
        Entity entity = this.getControllingPassenger();
        if (this.getTargetPos().isPresent()) {
            return false;
        } else if (entity instanceof PlayerEntity) {
            return ((PlayerEntity) entity).isUser();
        } else {
            return !this.world.isRemote;
        }
    }

    public void moveTowardsTarget() {
        this.getTargetPos().ifPresent(target -> {
            BlockPos entityPos = this.getPosition();
            int xOffset = entityPos.getX() - target.getX();
            int zOffset = entityPos.getZ() - target.getZ();
            float expectedYaw = (float) (-Math.toDegrees(Math.atan2(xOffset, zOffset)) + 180F);
            this.prevRotationYaw = this.rotationYaw;
            if (Math.abs(xOffset) + Math.abs(zOffset) > 5) {
                float delta = expectedYaw - this.rotationYaw;
                if (delta < 0) {
                    delta += 360;
                } else if (delta > 360) {
                    delta -= 360;
                }
                if (delta < 180 && delta > 1) {
                    this.rotationYaw++;

                } else if (delta > 180 && delta < 359) {
                    this.rotationYaw--;
                }
                float f = 0.05F;
                this.setMotion(this.getMotion().add(MathHelper.sin(-this.rotationYaw * ((float) Math.PI / 180F)) * f,
                        0.0D, MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * f));
            } else {

                this.getNavigationNetwork()
                        .map(this.navigator::getCurrentPoint)
                        .orElseGet(Optional::empty)
                        .ifPresent(point -> point.alertApproach(this.navigator, this));
            }
        });
    }

    public void setTargetPos(BlockPos blockPos) {
        this.getDataManager().set(TARGET_POS, Optional.ofNullable(blockPos));
    }

    public Optional<BlockPos> getTargetPos() {
        return this.getDataManager().get(TARGET_POS);
    }

    @Nonnull
    private LazyOptional<INavigationNetwork> getNavigationNetwork() {
        if (this.navigationNetwork == null) {
            this.navigationNetwork = this.getEntityWorld()
                    .getCapability(TransportCapabilities.NAVIGATION_NETWORK);
        }
        return this.navigationNetwork;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TransportCapabilities.NAVIGATOR) {
            return navigatorLazy.cast();
        }
        return super.getCapability(cap, side);
    }
}
