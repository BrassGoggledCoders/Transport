package xyz.brassgoggledcoders.transport.block.rail.turnout;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.switchmotor.ISwitchMotorBehavior;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SwitchRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> STRAIGHT_SHAPE = EnumProperty.create("straight", RailShape.class,
            railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST);
    public static final EnumProperty<RailShape> CURVE_SHAPE = EnumProperty.create("curve", RailShape.class,
            railShape -> railShape == RailShape.NORTH_EAST || railShape == RailShape.SOUTH_EAST ||
                    railShape == RailShape.SOUTH_WEST || railShape == RailShape.NORTH_WEST);
    public static final BooleanProperty DIVERGE = BooleanProperty.create("diverge");

    public SwitchRailBlock() {
        this(Block.Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
        this.setDefaultState(this.getDefaultState().with(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH)
                .with(CURVE_SHAPE, RailShape.SOUTH_EAST)
                .with(DIVERGE, false));
    }

    public SwitchRailBlock(Properties properties) {
        super(true, properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STRAIGHT_SHAPE, CURVE_SHAPE, DIVERGE);
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
                    blockState = blockState.with(CURVE_SHAPE, RailShape.SOUTH_EAST);
                } else {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.SOUTH_WEST);
                }
            } else if (firstClosest == Direction.SOUTH) {
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.NORTH_SOUTH);
                if (secondClosest == Direction.EAST) {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.NORTH_EAST);
                } else {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.NORTH_WEST);
                }
            } else if (firstClosest == Direction.WEST) {
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                if (secondClosest == Direction.NORTH) {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.NORTH_EAST);
                } else {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.SOUTH_EAST);
                }
            } else if (firstClosest == Direction.EAST) {
                blockState = blockState.with(STRAIGHT_SHAPE, RailShape.EAST_WEST);
                if (secondClosest == Direction.NORTH) {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.NORTH_WEST);
                } else {
                    blockState = blockState.with(CURVE_SHAPE, RailShape.SOUTH_WEST);
                }
            }
        }
        return blockState;
    }

    @Override
    @Nonnull
    public RailShape getRailDirection(BlockState state, IBlockReader blockReader, BlockPos pos, @Nullable AbstractMinecartEntity cart) {
        RailShape straightShape = state.get(STRAIGHT_SHAPE);
        RailShape curveShape = state.get(CURVE_SHAPE);
        if (cart != null) {
            return SwitchConfiguration.getRailShape(straightShape, curveShape, cart, direction ->
                    shouldDivert(blockReader, pos.offset(direction), pos, cart));
        } else {
            SwitchConfiguration configuration = SwitchConfiguration.get(straightShape, curveShape);
            if (configuration != null) {
                return shouldDivert(blockReader, pos.offset(configuration.getMotorSide()), pos, null) ?
                        curveShape : straightShape;
            }
        }
        return straightShape;
    }

    private boolean shouldDivert(IBlockReader blockReader, BlockPos motorLocation, BlockPos switchLocation,
                                 @Nullable AbstractMinecartEntity cart) {
        BlockState motorState = blockReader.getBlockState(motorLocation);
        ISwitchMotorBehavior motorBehavior = TransportAPI.TURNOUT_MOTOR_BEHAVIORS.get(motorState.getBlock());
        if (motorBehavior != null) {
            return motorBehavior.shouldDiverge(motorState, blockReader, motorLocation, switchLocation, cart);
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(@Nonnull BlockState state, Direction facing, BlockState facingState,
                                          IWorld world, BlockPos currentPos, BlockPos facingPos) {
        SwitchConfiguration configuration = SwitchConfiguration.get(state.get(STRAIGHT_SHAPE), state.get(CURVE_SHAPE));
        if (configuration.getMotorSide() == facing) {
            state = state.with(DIVERGE, this.shouldDivert(world, currentPos.offset(facing), currentPos, null));
        }
        return state;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (TransportItemTags.WRENCHES.contains(player.getHeldItem(hand).getItem())) {
            world.setBlockState(pos, state.cycle(CURVE_SHAPE), 3);
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    @Nonnull
    public IProperty<RailShape> getShapeProperty() {
        return STRAIGHT_SHAPE;
    }
}
