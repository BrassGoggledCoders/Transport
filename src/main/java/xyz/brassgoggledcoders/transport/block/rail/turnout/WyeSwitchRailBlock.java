package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import xyz.brassgoggledcoders.transport.api.TransportBlockStateProperties;
import xyz.brassgoggledcoders.transport.util.RailUtils;

import javax.annotation.Nonnull;

public class WyeSwitchRailBlock extends AbstractSwitchRailBlock {
    public static final EnumProperty<RailShape> SHAPE = TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE;
    public static final BooleanProperty NORTH_WEST = TransportBlockStateProperties.NORTH_WEST;

    public WyeSwitchRailBlock(Properties properties) {
        super(true, properties);
        this.setDefaultState(this.getDefaultState()
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(NORTH_WEST, true)
                .with(DIVERGE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, NORTH_WEST, DIVERGE);
    }

    @Override
    protected RailShape getStraightShape(SwitchConfiguration switchConfiguration) {
        switch (switchConfiguration.getDiverge()) {
            case NORTH_EAST:
                return RailShape.SOUTH_EAST;
            case NORTH_WEST:
                return RailShape.NORTH_EAST;
            case SOUTH_WEST:
                return RailShape.NORTH_WEST;
            default:
                return RailShape.SOUTH_WEST;
        }
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return RailUtils.setRailStateWithFacing(this.getDefaultState(), context);
    }

    @Override
    protected SwitchConfiguration getSwitchConfiguration(BlockState blockState) {
        if (blockState.get(SHAPE) == RailShape.NORTH_SOUTH) {
            return blockState.get(NORTH_WEST) ? SwitchConfiguration.NORTH_EAST_DIVERGE : SwitchConfiguration.SOUTH_WEST_DIVERGE;
        } else {
            return blockState.get(NORTH_WEST) ? SwitchConfiguration.WEST_NORTH_DIVERGE : SwitchConfiguration.EAST_SOUTH_DIVERGE;
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
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        switch (direction) {
            case CLOCKWISE_180:
                state = state.with(NORTH_WEST, !state.get(NORTH_WEST));
                break;
            case CLOCKWISE_90:
                if (state.get(NORTH_WEST)) {
                    if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
                        state = state.with(NORTH_WEST, false)
                                .with(SHAPE, RailShape.EAST_WEST);
                    } else {
                        state = state.with(SHAPE, RailShape.NORTH_SOUTH);
                    }
                } else {
                    if (state.get(SHAPE) == RailShape.EAST_WEST) {
                        state = state.with(NORTH_WEST, false)
                                .with(SHAPE, RailShape.NORTH_SOUTH);
                    } else {
                        state = state.with(NORTH_WEST, true)
                                .with(SHAPE, RailShape.EAST_WEST);
                    }
                }
                break;
            case COUNTERCLOCKWISE_90:
                if (state.get(NORTH_WEST)) {
                    if (state.get(SHAPE) == RailShape.EAST_WEST) {
                        state = state.with(NORTH_WEST, false)
                                .with(SHAPE, RailShape.NORTH_SOUTH);
                    } else {
                        state = state.with(SHAPE, RailShape.EAST_WEST);
                    }
                } else {
                    if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
                        state = state.with(NORTH_WEST, false)
                                .with(SHAPE, RailShape.EAST_WEST);
                    } else {
                        state = state.with(NORTH_WEST, true)
                                .with(SHAPE, RailShape.NORTH_SOUTH);
                    }
                }
                break;
            case NONE:
                break;
        }


        return state;
    }
}
