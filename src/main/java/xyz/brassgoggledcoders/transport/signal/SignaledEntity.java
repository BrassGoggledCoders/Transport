package xyz.brassgoggledcoders.transport.signal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import xyz.brassgoggledcoders.transport.util.LazyEntity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class SignaledEntity {

    private final LazyEntity<AbstractMinecart> entity;
    private SignalPoint lastSignalPoint;
    private SignalBlock currentSignalBlock;

    private BlockPos currentBlockPos;
    private BlockPos lastBlockPos;

    public SignaledEntity(UUID uuid, BlockPos currentBlockPos) {
        this(uuid, currentBlockPos, currentBlockPos);
    }

    public SignaledEntity(UUID uuid, BlockPos currentBlockPos, BlockPos lastBlockPos) {
        this.entity = new LazyEntity<>(uuid, AbstractMinecart.class);
        this.currentBlockPos = currentBlockPos;
        this.lastBlockPos = lastBlockPos;
    }

    public SignalPoint getLastSignalPoint() {
        return lastSignalPoint;
    }

    public void setLastSignalPoint(SignalPoint lastSignalPoint) {
        this.lastSignalPoint = lastSignalPoint;
    }

    public SignalBlock getCurrentSignalBlock() {
        return currentSignalBlock;
    }

    public void setCurrentSignalBlock(SignalBlock currentSignalBlock) {
        this.currentSignalBlock = currentSignalBlock;
    }

    public BlockPos getLastBlockPos() {
        return this.lastBlockPos;
    }

    public void update(ServerLevel serverLevel) {
        AbstractMinecart abstractMinecart = this.entity.apply(serverLevel);
        if (abstractMinecart != null) {
            if (!abstractMinecart.blockPosition().equals(this.currentBlockPos)) {
                this.lastBlockPos = this.currentBlockPos;
                this.currentBlockPos = abstractMinecart.blockPosition();
            }
        }
    }

    public CompoundTag toTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID("Entity", this.entity.getUuid());
        compoundTag.put("CurrentBlockPos", NbtUtils.writeBlockPos(this.currentBlockPos));
        compoundTag.put("LastBlockPos", NbtUtils.writeBlockPos(this.lastBlockPos));

        if (this.getLastSignalPoint() != null) {
            compoundTag.putUUID("LastSignalPoint", this.getLastSignalPoint().uuid());
        }
        if (this.getCurrentSignalBlock() != null) {
            compoundTag.putUUID("CurrentSignalBlock", this.getCurrentSignalBlock().getUuid());
        }
        return compoundTag;
    }

    public static SignaledEntity fromTag(
            CompoundTag compoundTag,
            Function<UUID, Optional<SignalPoint>> getSignalPoint,
            Function<UUID, Optional<SignalBlock>> getSignalBlock
    ) {
        SignaledEntity signaledEntity = new SignaledEntity(
                compoundTag.getUUID("Entity"),
                NbtUtils.readBlockPos(compoundTag.getCompound("CurrentBlockPos")),
                NbtUtils.readBlockPos(compoundTag.getCompound("LastBlockPos"))
        );
        if (compoundTag.contains("LastSignalPoint")) {
            getSignalPoint.apply(compoundTag.getUUID("LastSignalPoint"))
                    .ifPresent(signaledEntity::setLastSignalPoint);
        }
        if (compoundTag.contains("CurrentSignalBlock")) {
            getSignalBlock.apply(compoundTag.getUUID("CurrentSignalBlock"))
                    .ifPresent(signaledEntity::setCurrentSignalBlock);
        }
        return signaledEntity;
    }
}
