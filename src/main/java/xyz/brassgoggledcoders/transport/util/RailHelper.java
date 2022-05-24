package xyz.brassgoggledcoders.transport.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Nullable
    public static Direction getDirectionForRailShape(RailShape railShape, Direction start, boolean closest) {
        Tuple<Direction, Direction> railDirections = getDirectionsForRailShape(railShape);

        if (railDirections.getA() == start) {
            return closest ? railDirections.getA() : railDirections.getB();
        } else if (railDirections.getB() == start) {
            return closest ? railDirections.getB() : railDirections.getA();
        } else {
            return null;
        }
    }

    public static RailPlaceResult placeRail(BlockPlaceContext context, BlockState connectingState) {
        ItemStack railStack = context.getItemInHand();
        if (railStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BaseRailBlock railBlock) {
            Level level = context.getLevel();
            BlockPos position = context.getClickedPos();
            BlockState blockState = level.getBlockState(position);

            if (blockState.canBeReplaced(context)) {
                BlockPos belowPosition = position.below();
                BlockState belowState = level.getBlockState(belowPosition);
                if (belowState.isFaceSturdy(level, belowPosition, Direction.UP, SupportType.RIGID)) {
                    return new RailPlaceResult(
                            blockItem.place(context),
                            position
                    );
                } else if (railBlock.canMakeSlopes(railBlock.defaultBlockState(), level, belowPosition)) {
                    BlockPlaceContext belowContext = BlockPlaceContext.at(context, belowPosition, Direction.UP);
                    return new RailPlaceResult(
                            blockItem.place(belowContext),
                            belowPosition
                    );
                }
            } else {
                if (connectingState.getBlock() instanceof RailBlock connectingRailBlock) {
                    if (connectingRailBlock.canMakeSlopes(connectingState, level, position)) {
                        BlockPos abovePosition = position.above();
                        return new RailPlaceResult(
                                blockItem.place(BlockPlaceContext.at(context, abovePosition, Direction.UP)),
                                abovePosition
                        );
                    }
                }
            }
        }
        return new RailPlaceResult(
                InteractionResult.FAIL,
                context.getClickedPos()
        );
    }

    @Nonnull
    public static BlockPos findNextPotentialRail(BlockState currentState, BlockPos currentPosition, Direction exit, Level level) {
        if (exit.getAxis() != Direction.Axis.Y && currentState.getBlock() instanceof BaseRailBlock railBlock) {
            RailShape currentRailShape = railBlock.getRailDirection(currentState, level, currentPosition, null);
            if (currentRailShape.isAscending()) {
                Tuple<Direction, Direction> directions = getDirectionsForRailShape(currentRailShape);
                if (directions.getB() == exit) {
                    return currentPosition.offset(
                            exit.getStepX(),
                            1,
                            exit.getStepZ()
                    );
                } else {
                    return currentPosition.offset(exit.getNormal());
                }
            } else {
                BlockPos belowPos = currentPosition.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.isFaceSturdy(level, belowPos, Direction.UP, SupportType.RIGID)) {
                    return currentPosition.offset(exit.getNormal());
                } else {
                    return currentPosition.offset(
                            exit.getNormal().getX(),
                            -1,
                            exit.getNormal().getZ()
                    );
                }
            }
        }
        return currentPosition.offset(exit.getNormal());
    }

    public static boolean isRailShapeStraight(RailShape railShape) {
        return railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST;
    }
}
