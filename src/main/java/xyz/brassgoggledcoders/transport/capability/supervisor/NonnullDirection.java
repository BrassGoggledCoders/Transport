package xyz.brassgoggledcoders.transport.capability.supervisor;

import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum NonnullDirection {
    DOWN(Direction.DOWN),
    UP(Direction.UP),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    WEST(Direction.WEST),
    EAST(Direction.EAST),
    NULL(null);

    private final Direction direction;

    NonnullDirection(@Nullable Direction direction) {
        this.direction = direction;
    }

    @Nullable
    public Direction getDirection() {
        return direction;
    }

    @Nonnull
    public static NonnullDirection fromDirection(@Nullable Direction direction) {
        if (direction != null) {
            for (NonnullDirection nonnullDirection : values()) {
                if (nonnullDirection.getDirection() == direction) {
                    return nonnullDirection;
                }
            }
        }
        return NULL;
    }
}
