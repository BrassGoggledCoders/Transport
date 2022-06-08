package xyz.brassgoggledcoders.transport.signal;

import net.minecraft.nbt.CompoundTag;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class SignaledEntity {
    private SignalPoint lastSignalPoint;
    private SignalBlock currentSignalBlock;

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

    public CompoundTag toTag() {
        CompoundTag compoundTag = new CompoundTag();
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
        SignaledEntity signaledEntity = new SignaledEntity();
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
