package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SwitchRailBlock extends AbstractSwitchRailBlock {
    public static final EnumProperty<RailShape> DIVERGE_SHAPE = EnumProperty.create("diverge_shape", RailShape.class,
            railShape -> railShape == RailShape.NORTH_EAST || railShape == RailShape.SOUTH_EAST ||
                    railShape == RailShape.SOUTH_WEST || railShape == RailShape.NORTH_WEST);
    public static final EnumProperty<RailShape> STRAIGHT_SHAPE = EnumProperty.create("straight_shape",
            RailShape.class, railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST);

    public SwitchRailBlock() {
        this(Block.Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
    }

    public SwitchRailBlock(Properties properties) {
        super(true, properties);
        this.setDefaultState(this.getDefaultState().with(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH)
                .with(DIVERGE_SHAPE, RailShape.SOUTH_EAST)
                .with(DIVERGE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STRAIGHT_SHAPE, DIVERGE_SHAPE, DIVERGE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockState = this.getDefaultState();
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
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                if (secondClosest == Direction.EAST) {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.SOUTH_EAST);
                } else {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.SOUTH_WEST);
                }
            } else if (firstClosest == Direction.SOUTH) {
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                if (secondClosest == Direction.EAST) {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.NORTH_EAST);
                } else {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.NORTH_WEST);
                }
            } else if (firstClosest == Direction.WEST) {
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                if (secondClosest == Direction.NORTH) {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.NORTH_EAST);
                } else {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.SOUTH_EAST);
                }
            } else if (firstClosest == Direction.EAST) {
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                if (secondClosest == Direction.NORTH) {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.NORTH_WEST);
                } else {
                    blockState = blockState.with(DIVERGE_SHAPE, RailShape.SOUTH_WEST);
                }
            }
        }
        return blockState;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (TransportItemTags.WRENCHES.contains(player.getHeldItem(hand).getItem())) {
            world.setBlockState(pos, state.func_235896_a_(DIVERGE_SHAPE), 3);
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    @Nonnull
    public Property<RailShape> getShapeProperty() {
        return STRAIGHT_SHAPE;
    }

    @Override
    protected RailShape getStraightShape(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getStraight();
    }

    @Override
    protected SwitchConfiguration getSwitchConfiguration(BlockState blockState) {
        return SwitchConfiguration.get(blockState.get(STRAIGHT_SHAPE), blockState.get(DIVERGE_SHAPE));
    }

    @Override
    protected Direction getMotorDirection(SwitchConfiguration switchConfiguration) {
        return switchConfiguration.getDivergentSide().getOpposite();
    }
}
