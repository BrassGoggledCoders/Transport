package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;

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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE, INVERTED, POWERED, WATERLOGGED);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected BlockState updateState(BlockState pState, Level pLevel, BlockPos pPos, boolean pIsMoving) {
        BlockState newState = this.updateDir(pLevel, pPos, pState, true);

        newState = checkInvertedOnChange(pState, newState, pLevel, pPos);

        newState.neighborChanged(pLevel, pPos, this, pPos, pIsMoving);

        return newState;
    }

    private BlockState checkInvertedOnChange(BlockState oldBlockState, BlockState newBlockState, Level pLevel, BlockPos pPos) {
        RailShape oldRailShape = oldBlockState.getValue(RAIL_SHAPE);
        RailShape newRailShape = newBlockState.getValue(RAIL_SHAPE);

        if (oldRailShape == RailShape.NORTH_SOUTH && newRailShape == RailShape.ASCENDING_SOUTH ||
                oldRailShape == RailShape.EAST_WEST && newRailShape == RailShape.ASCENDING_EAST ||
                oldRailShape == RailShape.ASCENDING_SOUTH && newRailShape == RailShape.NORTH_SOUTH ||
                oldRailShape == RailShape.ASCENDING_EAST && newRailShape == RailShape.EAST_WEST
        ) {
            newBlockState = newBlockState.cycle(INVERTED);
            pLevel.setBlock(pPos, newBlockState, Block.UPDATE_ALL);
        }

        return newBlockState;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.updateState(pState, pLevel, pPos, pIsMoving);
        } else {
            checkInvertedOnChange(pOldState, pState, pLevel, pPos);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(POWERED)) {
            boolean inverted = state.getValue(INVERTED);
            RailShape railShape = state.getValue(RAIL_SHAPE);
            Vec3 normalized = cart.getDeltaMovement().normalize();

            Direction direction = Direction.fromNormal(new BlockPos(normalized));

            if (direction != null) {
                Vec3 cartPos = cart.getPosition(1);
                Vec3 blockPos = railShape.isAscending() ? Vec3.atCenterOf(pos) : Vec3.atBottomCenterOf(pos);
                if (cartPos.distanceTo(blockPos) < 0.25) {
                    boolean shouldReverse = switch (railShape) {
                        case ASCENDING_NORTH, NORTH_SOUTH -> (direction == Direction.NORTH && inverted) || (direction == Direction.SOUTH && !inverted);
                        case ASCENDING_SOUTH -> direction == Direction.SOUTH && inverted;
                        case ASCENDING_EAST -> direction == Direction.EAST && inverted;
                        case ASCENDING_WEST -> direction == Direction.WEST && inverted;
                        case EAST_WEST -> (direction == Direction.EAST && !inverted) || (direction == Direction.WEST && inverted);
                        default -> false;
                    };

                    if (shouldReverse) {
                        Vec3 delta = cart.getDeltaMovement();
                        cart.setDeltaMovement(new Vec3(
                                delta.x() * -1D,
                                railShape.isAscending() ? delta.y() * -1D : delta.y(),
                                delta.z() * -1D
                        ));
                    }
                }
            } else {
                Vec3i boostDirection = switch (railShape) {
                    case NORTH_SOUTH, ASCENDING_NORTH -> inverted ? Direction.SOUTH.getNormal() : Direction.NORTH.getNormal();
                    case ASCENDING_SOUTH -> inverted ? Direction.NORTH.getNormal() : Direction.SOUTH.getNormal();
                    case ASCENDING_EAST -> inverted ? Direction.WEST.getNormal() : Direction.EAST.getNormal();
                    case EAST_WEST, ASCENDING_WEST -> inverted ? Direction.EAST.getNormal() : Direction.WEST.getNormal();
                    default -> Vec3i.ZERO;
                };
                cart.setDeltaMovement(Vec3.atLowerCornerOf(boostDirection));
            }
        }
    }

    @Override
    @Nonnull
    public Property<RailShape> getShapeProperty() {
        return RAIL_SHAPE;
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
}