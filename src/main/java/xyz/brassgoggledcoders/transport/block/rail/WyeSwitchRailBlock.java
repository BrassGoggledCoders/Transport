package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
public class WyeSwitchRailBlock extends AbstractSwitchRailBlock {
    public static final EnumProperty<RailShape> SHAPE = TransportBlockStateProperties.FLAT_STRAIGHT_RAIL_SHAPE;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public WyeSwitchRailBlock(Properties properties) {
        super(true, properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(INVERTED, true)
                .setValue(DIVERGE, false)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, INVERTED, DIVERGE, WATERLOGGED);
    }

    @Override
    @NotNull
    protected RailShape getStraightShape(SwitchConfiguration switchConfiguration) {
        return switch (switchConfiguration.getDiverge()) {
            case NORTH_EAST -> RailShape.SOUTH_EAST;
            case NORTH_WEST -> RailShape.NORTH_EAST;
            case SOUTH_WEST -> RailShape.NORTH_WEST;
            default -> RailShape.SOUTH_WEST;
        };
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        if (blockState != null) {
            Direction horizontalDirection = context.getHorizontalDirection();
            blockState = blockState.setValue(INVERTED, switch (blockState.getValue(SHAPE)) {
                case NORTH_SOUTH -> horizontalDirection == Direction.SOUTH;
                case EAST_WEST -> horizontalDirection == Direction.EAST;
                default -> false;
            });
        }
        return blockState;
    }

    @Override
    protected SwitchConfiguration getSwitchConfiguration(BlockState blockState) {
        if (blockState.getValue(SHAPE) == RailShape.NORTH_SOUTH) {
            return !blockState.getValue(INVERTED) ? SwitchConfiguration.NORTH_EAST_DIVERGE : SwitchConfiguration.SOUTH_WEST_DIVERGE;
        } else {
            return !blockState.getValue(INVERTED) ? SwitchConfiguration.WEST_NORTH_DIVERGE : SwitchConfiguration.EAST_SOUTH_DIVERGE;
        }
    }

    @Override
    protected Direction getMotorDirection(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getNarrowSide().getOpposite();
    }

    @Override
    @Nonnull
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BlockState rotate(BlockState state, Rotation pRotation) {
        switch (pRotation) {
            case CLOCKWISE_180:
                state = state.setValue(INVERTED, !state.getValue(INVERTED));
                break;
            case CLOCKWISE_90:
                if (state.getValue(INVERTED)) {
                    if (state.getValue(SHAPE) == RailShape.NORTH_SOUTH) {
                        state = state.setValue(INVERTED, false)
                                .setValue(SHAPE, RailShape.EAST_WEST);
                    } else {
                        state = state.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    }
                } else {
                    if (state.getValue(SHAPE) == RailShape.EAST_WEST) {
                        state = state.setValue(INVERTED, false)
                                .setValue(SHAPE, RailShape.NORTH_SOUTH);
                    } else {
                        state = state.setValue(INVERTED, true)
                                .setValue(SHAPE, RailShape.EAST_WEST);
                    }
                }
                break;
            case COUNTERCLOCKWISE_90:
                if (state.getValue(INVERTED)) {
                    if (state.getValue(SHAPE) == RailShape.EAST_WEST) {
                        state = state.setValue(INVERTED, false)
                                .setValue(SHAPE, RailShape.NORTH_SOUTH);
                    } else {
                        state = state.setValue(SHAPE, RailShape.EAST_WEST);
                    }
                } else {
                    if (state.getValue(SHAPE) == RailShape.NORTH_SOUTH) {
                        state = state.setValue(INVERTED, false)
                                .setValue(SHAPE, RailShape.EAST_WEST);
                    } else {
                        state = state.setValue(INVERTED, true)
                                .setValue(SHAPE, RailShape.NORTH_SOUTH);
                    }
                }
                break;
            case NONE:
                break;
        }


        return state;
    }
}