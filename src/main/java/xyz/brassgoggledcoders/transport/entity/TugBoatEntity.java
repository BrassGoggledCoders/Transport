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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNetwork;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNetworks;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class TugBoatEntity extends BoatEntity {
    private static final DataParameter<Optional<BlockPos>> TARGET_POS = EntityDataManager.createKey(TugBoatEntity.class,
            DataSerializers.OPTIONAL_BLOCK_POS);

    private RoutingNode targetStation;

    public TugBoatEntity(EntityType<? extends BoatEntity> type, World world) {
        super(type, world);
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
        this.checkTargetPos();
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
                    this.rotationYaw += 1.25;

                } else if (delta > 180 && delta < 359) {
                    this.rotationYaw -= 1.25;
                }
                float f = 0.05F;
                this.setMotion(this.getMotion().add(MathHelper.sin(-this.rotationYaw * ((float) Math.PI / 180F)) * f,
                        0.0D, MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * f));
            }
        });
    }

    private void checkTargetPos() {
        Optional<BlockPos> optTargetPos = this.getTargetPos();
        if (optTargetPos.isPresent()) {
            BlockPos targetPos = optTargetPos.get();
            if (targetPos.manhattanDistance(this.getPosition()) < 8) {
                this.setTargetPos(null);
            }
        } else {
            RoutingNetwork routingNetwork = RoutingNetworks.SHIP.getFor(this.getEntityWorld());
            if (routingNetwork != null) {
                RoutingNode nextStation = null;
                if (targetStation == null || !targetStation.isValid()) {
                    Optional<RoutingNode> station = routingNetwork.getClosestStation(this.getPosition());
                    nextStation = station.orElse(null);
                } else {
                    List<RoutingNode> stations = routingNetwork.getConnectedStations(targetStation);
                    if (!stations.isEmpty()) {
                        nextStation = stations.get(rand.nextInt(stations.size()));
                    }
                }
                if (nextStation != null) {
                    this.targetStation = nextStation;
                    this.setTargetPos(nextStation.getPosition());
                }
            }
        }
    }

    public void setTargetPos(BlockPos blockPos) {
        this.getDataManager().set(TARGET_POS, Optional.ofNullable(blockPos));
    }

    public Optional<BlockPos> getTargetPos() {
        return this.getDataManager().get(TARGET_POS);
    }
}
