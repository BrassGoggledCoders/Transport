package xyz.brassgoggledcoders.transport.block.rail.portal;

import net.minecraft.core.Direction.Axis;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum PortalState implements StringRepresentable {
    X_AXIS(Axis.X),
    Z_AXIS(Axis.Z),
    NONE(null);

    private final Axis axis;

    PortalState(Axis axis) {
        this.axis = axis;
    }

    @Nullable
    public Axis getAxis() {
        return this.axis;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ENGLISH);
    }

    public static PortalState fromAxis(@Nullable Axis axis) {
        if (axis == null) {
            return NONE;
        } else {
            return switch (axis) {
                case X -> X_AXIS;
                case Y -> NONE;
                case Z -> Z_AXIS;
            };
        }
    }
}
