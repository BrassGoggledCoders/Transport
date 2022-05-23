package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.util.BlockStateHelper;

import javax.annotation.ParametersAreNonnullByDefault;

public class SwitchRailBlock extends AbstractSwitchRailBlock {
    public static final EnumProperty<RailShape> DIVERGE_SHAPE = EnumProperty.create("diverge_shape", RailShape.class,
            railShape -> railShape == RailShape.NORTH_EAST || railShape == RailShape.SOUTH_EAST ||
                    railShape == RailShape.SOUTH_WEST || railShape == RailShape.NORTH_WEST);
    public static final EnumProperty<RailShape> STRAIGHT_SHAPE = EnumProperty.create("straight_shape",
            RailShape.class, railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST);

    public static final BooleanProperty WATER_LOGGED = BaseRailBlock.WATERLOGGED;

    public SwitchRailBlock(Properties properties) {
        super(true, properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH)
                .setValue(DIVERGE_SHAPE, RailShape.SOUTH_EAST)
                .setValue(WATER_LOGGED, false)
                .setValue(DIVERGE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STRAIGHT_SHAPE, DIVERGE_SHAPE, DIVERGE, WATER_LOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.defaultBlockState();
        Direction[] nearestDirections = context.getNearestLookingDirections();
        if (nearestDirections.length >= 3) {
            int currentCheck = 0;
            Direction firstClosest = nearestDirections[currentCheck++];
            if (firstClosest.getAxis() == Direction.Axis.Y) {
                firstClosest = nearestDirections[currentCheck++];
            }
            Direction secondClosest = nearestDirections[currentCheck++];
            if (secondClosest.getAxis() == Direction.Axis.Y) {
                secondClosest = nearestDirections[currentCheck];
            }
            if (firstClosest == Direction.NORTH) {
                blockState = blockState.setValue(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                if (secondClosest == Direction.EAST) {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.SOUTH_EAST);
                } else {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.SOUTH_WEST);
                }
            } else if (firstClosest == Direction.SOUTH) {
                blockState = blockState.setValue(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                if (secondClosest == Direction.EAST) {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.NORTH_EAST);
                } else {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.NORTH_WEST);
                }
            } else if (firstClosest == Direction.WEST) {
                blockState = blockState.setValue(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                if (secondClosest == Direction.NORTH) {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.NORTH_EAST);
                } else {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.SOUTH_EAST);
                }
            } else if (firstClosest == Direction.EAST) {
                blockState = blockState.setValue(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                if (secondClosest == Direction.NORTH) {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.NORTH_WEST);
                } else {
                    blockState = blockState.setValue(DIVERGE_SHAPE, RailShape.SOUTH_WEST);
                }
            }
        }

        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;

        return blockState.setValue(WATERLOGGED, flag);
    }

    @Override
    @NotNull
    public Property<RailShape> getShapeProperty() {
        return STRAIGHT_SHAPE;
    }

    @Override
    protected RailShape getStraightShape(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getStraight();
    }

    @Override
    protected SwitchConfiguration getSwitchConfiguration(BlockState blockState) {
        return SwitchConfiguration.get(blockState.getValue(STRAIGHT_SHAPE), blockState.getValue(DIVERGE_SHAPE));
    }

    @Override
    protected Direction getMotorDirection(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getDivergentSide().getOpposite();
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation pRotation) {
        switch (pRotation) {
            case CLOCKWISE_180:
                state = state.cycle(DIVERGE_SHAPE)
                        .cycle(DIVERGE_SHAPE);
                break;
            case CLOCKWISE_90:
                RailShape clockwiseShape = state.getValue(STRAIGHT_SHAPE);
                switch (state.getValue(DIVERGE_SHAPE)) {
                    case NORTH_EAST:
                    case SOUTH_WEST:
                        if (clockwiseShape == RailShape.NORTH_SOUTH) {
                            state = state.setValue(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                        } else {
                            state = state.cycle(DIVERGE_SHAPE);
                        }
                        break;
                    case SOUTH_EAST:
                    case NORTH_WEST:
                        if (clockwiseShape == RailShape.EAST_WEST) {
                            state = state.setValue(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                        } else {
                            state = BlockStateHelper.cyclePrevious(state, DIVERGE_SHAPE);
                        }
                        break;
                }
                break;
            case COUNTERCLOCKWISE_90:
                RailShape counterClockWiseShape = state.getValue(STRAIGHT_SHAPE);
                switch (state.getValue(DIVERGE_SHAPE)) {
                    case NORTH_EAST:
                    case SOUTH_WEST:
                        if (counterClockWiseShape == RailShape.EAST_WEST) {
                            state = state.setValue(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                        } else {
                            state = BlockStateHelper.cyclePrevious(state, DIVERGE_SHAPE);
                        }
                        break;
                    case SOUTH_EAST:
                    case NORTH_WEST:
                        if (counterClockWiseShape == RailShape.NORTH_SOUTH) {
                            state = state.setValue(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                        } else {
                            state = BlockStateHelper.cyclePrevious(state, DIVERGE_SHAPE);
                        }
                        break;
                }
                break;
            case NONE:
                break;
        }
        return state;
    }
}
