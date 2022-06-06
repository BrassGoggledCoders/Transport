package xyz.brassgoggledcoders.transport.signal;

import java.util.UUID;

public class SignaledEntity {
    private final UUID uuid;
    private SignalPoint lastSignalPoint;
    private SignalBlock currentSignalBlock;

    public SignaledEntity(UUID uuid) {
        this.uuid = uuid;
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
}
