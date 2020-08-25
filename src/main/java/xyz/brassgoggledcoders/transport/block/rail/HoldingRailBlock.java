package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportBlockStateProperties;
import xyz.brassgoggledcoders.transport.api.entity.IHoldable;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.util.RailUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class HoldingRailBlock extends AbstractRailBlock {
    public static final BooleanProperty NORTH_WEST = TransportBlockStateProperties.NORTH_WEST;
    public static final EnumProperty<RailShape> SHAPE = TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public HoldingRailBlock(Block.Properties properties) {
        super(true, properties);
        this.setDefaultState(this.getDefaultState().with(POWERED, false)
                .with(SHAPE, RailShape.NORTH_SOUTH)
                .with(NORTH_WEST, true));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, NORTH_WEST, POWERED);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                             BlockRayTraceResult rayTraceResult) {
        if (TransportItemTags.WRENCHES.contains(player.getHeldItem(hand).getItem())) {
            state = state.with(NORTH_WEST, !state.get(NORTH_WEST));
            world.setBlockState(pos, state, 3);
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
        if (state.get(POWERED)) {
            handleGo(state, cart);
        } else {
            handleStop(cart);
        }
    }

    public static void handleGo(BlockState blockState, AbstractMinecartEntity minecartEntity) {
        float speedIncrease = .5f;
        if (blockState.get(NORTH_WEST)) {
            speedIncrease *= -1;
        }
        Vector3d motion = minecartEntity.getMotion();
        Entity leader = TransportAPI.getConnectionChecker().getLeader(minecartEntity);
        if (leader == null || (leader instanceof AbstractMinecartEntity &&
                !((AbstractMinecartEntity) leader).isPoweredCart())) {
            if (blockState.get(SHAPE) == RailShape.NORTH_SOUTH) {
                minecartEntity.setMotion(motion.add(0, 0, speedIncrease));
            } else {
                minecartEntity.setMotion(motion.add(speedIncrease, 0, 0));
            }
        }
        if (minecartEntity instanceof IHoldable) {
            ((IHoldable) minecartEntity).onRelease();
            if (blockState.get(SHAPE) == RailShape.NORTH_SOUTH) {
                ((IHoldable) minecartEntity).push(0, speedIncrease);
            } else {
                ((IHoldable) minecartEntity).push(speedIncrease, 0);
            }
        }
    }

    public static void handleStop(AbstractMinecartEntity minecartEntity) {
        minecartEntity.setMotion(Vector3d.ZERO);
        if (minecartEntity instanceof IHoldable) {
            ((IHoldable) minecartEntity).onHeld();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void updateState(BlockState state, World world, BlockPos pos, Block block) {
        boolean isStatePowered = state.get(POWERED);
        boolean isWorldPowered = world.isBlockPowered(pos);

        if (isWorldPowered != isStatePowered) {
            world.setBlockState(pos, state.with(POWERED, isWorldPowered), 3);
            world.notifyNeighborsOfStateChange(pos.down(), this);
        }
        super.updateState(state, world, pos, block);
    }


    @Override
    @Nonnull
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return RailUtils.setRailStateWithFacing(this.getDefaultState(), context);
    }
}
