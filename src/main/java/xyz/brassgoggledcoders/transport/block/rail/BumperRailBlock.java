package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.NotNull;

public class BumperRailBlock extends BaseRailBlock {
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final EnumProperty<RailShape> SHAPE = TransportBlockStateProperties.FLAT_STRAIGHT_RAIL_SHAPE;

    public BumperRailBlock(Properties properties) {
        super(true, properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INVERTED, SHAPE, BaseRailBlock.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        if (blockState != null) {
            Direction horizontalDirection = context.getHorizontalDirection();
            blockState = blockState.setValue(INVERTED, switch (blockState.getValue(SHAPE)) {
                case EAST_WEST -> horizontalDirection == Direction.WEST;
                case NORTH_SOUTH -> horizontalDirection == Direction.SOUTH;
                default -> false;
            });
        }
        return blockState;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    @Override
    @NotNull
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
}