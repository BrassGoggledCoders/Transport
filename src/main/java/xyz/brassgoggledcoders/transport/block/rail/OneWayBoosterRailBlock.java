package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(POWERED)) {
            boolean inverted = state.getValue(INVERTED);
            RailShape railShape = state.getValue(RAIL_SHAPE);
            Vec3 deltaMotion = cart.getDeltaMovement();

            boolean shouldReverse = switch (railShape) {
                case ASCENDING_NORTH -> deltaMotion.z() < 0 && inverted;
                case ASCENDING_SOUTH, NORTH_SOUTH -> deltaMotion.z() > 0 && inverted;
                case ASCENDING_EAST -> deltaMotion.x() < 0 && inverted;
                case ASCENDING_WEST, EAST_WEST -> deltaMotion.x() > 0 && inverted;
                default -> false;
            };

            if (shouldReverse) {
                cart.setDeltaMovement(deltaMotion.reverse());
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
                case ASCENDING_WEST, EAST_WEST-> horizontalDirection == Direction.EAST;
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
        return super.mirror(pState, pMirror)
                .cycle(INVERTED);
    }
}