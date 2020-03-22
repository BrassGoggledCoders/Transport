package xyz.brassgoggledcoders.transport.util;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.transport.api.TransportBlockStateProperties;

public class RailUtils {
    public static BlockState setRailStateWithFacing(BlockState blockState, BlockItemUseContext context) {
        if (blockState.has(TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE) &&
                blockState.has(TransportBlockStateProperties.NORTH_WEST)) {
            Direction direction = context.getPlacementHorizontalFacing();
            switch (direction) {
                case NORTH:
                case SOUTH:
                    blockState = blockState.with(TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE, RailShape.NORTH_SOUTH);
                    break;
                case WEST:
                case EAST:
                    blockState = blockState.with(TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE, RailShape.EAST_WEST);
                    break;
            }
            switch (direction) {
                case EAST:
                case SOUTH:
                    blockState = blockState.with(TransportBlockStateProperties.NORTH_WEST, false);
                    break;
                case WEST:
                case NORTH:
                    blockState = blockState.with(TransportBlockStateProperties.NORTH_WEST, true);
                    break;
            }
        }
        return blockState;
    }
}
