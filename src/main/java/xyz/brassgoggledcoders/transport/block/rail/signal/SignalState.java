package xyz.brassgoggledcoders.transport.block.rail.signal;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SignalState implements StringRepresentable {
    PROCEED("proceed"),
    SLOW("slow"),
    STOP("stop");

    private final String name;

    SignalState(String name) {
        this.name = name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
}
