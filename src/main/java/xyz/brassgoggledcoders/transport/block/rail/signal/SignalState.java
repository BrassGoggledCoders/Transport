package xyz.brassgoggledcoders.transport.block.rail.signal;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SignalState implements StringRepresentable {
    PROCEED("proceed", true),
    SLOW("slow", true),
    STOP("stop", false);

    private final String name;
    private final boolean powered;

    SignalState(String name, boolean powered) {
        this.name = name;
        this.powered = powered;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    public boolean isPowered() {
        return powered;
    }
}
