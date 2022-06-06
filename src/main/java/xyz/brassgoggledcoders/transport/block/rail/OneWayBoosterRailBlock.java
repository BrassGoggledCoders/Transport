package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import xyz.brassgoggledcoders.transport.util.MinecartHelper;
import xyz.brassgoggledcoders.transport.util.RailHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class OneWayBoosterRailBlock extends PoweredRailBlock {
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final EnumProperty<RailShape> RAIL_SHAPE = PoweredRailBlock.SHAPE;
    public static final BooleanProperty POWERED = PoweredRailBlock.POWERED;
    public static final BooleanProperty WATERLOGGED = BaseRailBlock.WATERLOGGED;

    public OneWayBoosterRailBlock(Properties properties) {
        super(properties, true);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(RAIL_SHAPE, RailShape.NORTH_SOUTH)
                .setValue(INVERTED, Boolean.TRUE)
                .setValue(POWERED, Boolean.FALSE)
                .setValue(WATERLOGGED, Boolean.FALSE)
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.updateState(pState, pLevel, pPos, pIsMoving);
        } else {
            RailHelper.checkInvertedOnChange(RAIL_SHAPE, pOldState, pState, pLevel, pPos);
        }
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected BlockState updateState(BlockState pState, Level pLevel, BlockPos pPos, boolean pIsMoving) {
        BlockState newState = this.updateDir(pLevel, pPos, pState, true);

        newState = RailHelper.checkInvertedOnChange(RAIL_SHAPE, pState, newState, pLevel, pPos);

        newState.neighborChanged(pLevel, pPos, this, pPos, pIsMoving);

        return newState;
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        if (blockState != null) {
            Direction horizontalDirection = context.getHorizontalDirection();
            blockState = blockState.setValue(INVERTED, switch (blockState.getValue(RAIL_SHAPE)) {
                case ASCENDING_NORTH, NORTH_SOUTH -> horizontalDirection == Direction.SOUTH;
                case ASCENDING_SOUTH -> horizontalDirection == Direction.NORTH;
                case ASCENDING_EAST -> horizontalDirection == Direction.WEST;
                case ASCENDING_WEST, EAST_WEST -> horizontalDirection == Direction.EAST;
                default -> false;
            });
        }
        return blockState;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(POWERED)) {
            MinecartHelper.boostMinecart(state, pos, RAIL_SHAPE, cart);
        }
    }

    @Override
    @Nonnull
    public Property<RailShape> getShapeProperty() {
        return RAIL_SHAPE;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        BlockState newState = super.rotate(pState, pRotation);

        RailShape oldShape = pState.getValue(RAIL_SHAPE);
        if (!oldShape.isAscending()) {
            newState = switch (pRotation) {
                case CLOCKWISE_180 -> newState.cycle(INVERTED);
                case CLOCKWISE_90 -> {
                    if (oldShape == RailShape.NORTH_SOUTH) {
                        yield newState.cycle(INVERTED);
                    }
                    yield newState;
                }
                case COUNTERCLOCKWISE_90 -> {
                    if (oldShape == RailShape.EAST_WEST) {
                        yield newState.cycle(INVERTED);
                    }
                    yield newState;
                }
                default -> newState;
            };

        }

        return newState;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        BlockState blockState = super.mirror(pState, pMirror);
        if (pMirror != Mirror.NONE && !blockState.getValue(RAIL_SHAPE).isAscending()) {
            blockState = blockState.cycle(INVERTED);
        }
        return blockState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE, INVERTED, POWERED, WATERLOGGED);
    }
}