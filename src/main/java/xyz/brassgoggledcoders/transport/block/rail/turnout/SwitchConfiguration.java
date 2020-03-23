package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;

import java.util.function.Function;
import java.util.function.Supplier;

public enum SwitchConfiguration {
    NORTH_EAST_DIVERGE(RailShape.NORTH_SOUTH, RailShape.SOUTH_EAST, Direction.SOUTH, Direction.EAST),
    NORTH_WEST_DIVERGE(RailShape.NORTH_SOUTH, RailShape.SOUTH_WEST, Direction.SOUTH, Direction.WEST),
    SOUTH_EAST_DIVERGE(RailShape.NORTH_SOUTH, RailShape.NORTH_EAST, Direction.NORTH, Direction.EAST),
    SOUTH_WEST_DIVERGE(RailShape.NORTH_SOUTH, RailShape.NORTH_WEST, Direction.NORTH, Direction.WEST),
    EAST_NORTH_DIVERGE(RailShape.EAST_WEST, RailShape.NORTH_WEST, Direction.WEST, Direction.NORTH),
    EAST_SOUTH_DIVERGE(RailShape.EAST_WEST, RailShape.SOUTH_WEST, Direction.WEST, Direction.SOUTH),
    WEST_NORTH_DIVERGE(RailShape.EAST_WEST, RailShape.NORTH_EAST, Direction.EAST, Direction.NORTH),
    WEST_SOUTH_DIVERGE(RailShape.EAST_WEST, RailShape.SOUTH_EAST, Direction.EAST, Direction.SOUTH);

    private final Direction narrowSide;
    private final Direction divergentSide;

    private final RailShape straight;
    private final RailShape diverge;

    SwitchConfiguration(RailShape straight, RailShape diverge, Direction narrowSide, Direction divergentSide) {
        this.narrowSide = narrowSide;
        this.divergentSide = divergentSide;
        this.straight = straight;
        this.diverge = diverge;
    }

    public RailShape getDiverge() {
        return diverge;
    }

    public RailShape getStraight() {
        return straight;
    }

    public Direction getDivergentSide() {
        return divergentSide;
    }

    public Direction getNarrowSide() {
        return narrowSide;
    }

    public static SwitchConfiguration get(RailShape straight, RailShape diverge) {
        for (SwitchConfiguration switchConfiguration : values()) {
            if (switchConfiguration.straight == straight && switchConfiguration.diverge == diverge) {
                return switchConfiguration;
            }
        }
        throw new IllegalStateException("No Valid Switch Configuration Found");
    }
}
