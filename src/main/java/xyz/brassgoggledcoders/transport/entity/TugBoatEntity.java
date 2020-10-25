package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TugBoatEntity extends BoatEntity {
    private static final DataParameter<Optional<BlockPos>> TARGET_POS = EntityDataManager.createKey(TugBoatEntity.class,
            DataSerializers.OPTIONAL_BLOCK_POS);
    private BlockPos targetedPosition = null;

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
        if (!this.getEntityWorld().isRemote() && this.getEntityWorld().rand.nextInt(50) == 0) {
            if (this.targetedPosition != null) {
                this.getEntityWorld().setBlockState(this.targetedPosition, Blocks.AIR.getDefaultState());
            }
            this.targetedPosition = new BlockPos(this.getPosX() + this.getEntityWorld().rand.nextInt(20) - 10,
                    this.getPosY() + 1, this.getPosZ() + this.getEntityWorld().rand.nextInt(20) - 10);
            this.dataManager.set(TARGET_POS, Optional.of(targetedPosition));
            this.getEntityWorld().setBlockState(this.targetedPosition, Blocks.STONE.getDefaultState());
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
        } else if (this.dataManager.get(TARGET_POS).isPresent()) {
            this.updateMotion();
            this.moveTowardsTarget();
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

    public void moveTowardsTarget() {
        Optional<BlockPos> currentTarget = this.getDataManager().get(TARGET_POS);
        currentTarget.ifPresent(target -> {
            BlockPos entityPos = this.getPosition();
            int xOffset = entityPos.getX() - target.getX();
            int zOffset = entityPos.getZ() - target.getZ();
            double expectedYaw = Math.toDegrees(Math.atan2(xOffset, zOffset));
            this.rotationYaw = (float) (-expectedYaw) + 180;
        });
    }
}
