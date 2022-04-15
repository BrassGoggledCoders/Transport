package xyz.brassgoggledcoders.transport.util;

import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RailHelper {
    public static Tuple<Direction, Direction> getDirectionsForRailShape(RailShape railShape) {
        return switch (railShape) {
            case NORTH_SOUTH, ASCENDING_SOUTH -> new Tuple<>(Direction.NORTH, Direction.SOUTH);
            case EAST_WEST, ASCENDING_WEST -> new Tuple<>(Direction.EAST, Direction.WEST);
            case ASCENDING_EAST -> new Tuple<>(Direction.WEST, Direction.EAST);
            case ASCENDING_NORTH -> new Tuple<>(Direction.SOUTH, Direction.NORTH);
            case NORTH_EAST -> new Tuple<>(Direction.NORTH, Direction.EAST);
            case NORTH_WEST -> new Tuple<>(Direction.WEST, Direction.NORTH);
            case SOUTH_EAST -> new Tuple<>(Direction.EAST, Direction.SOUTH);
            case SOUTH_WEST -> new Tuple<>(Direction.SOUTH, Direction.WEST);
        };
    }
}
