package xyz.brassgoggledcoders.transport.block.rail;

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
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;

import javax.annotation.Nonnull;

public class HoldingRailBlock extends AbstractRailBlock {
    public static final BooleanProperty NORTH_WEST = BooleanProperty.create("north_west");
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class,
            railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST);
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public HoldingRailBlock() {
        this(Block.Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
    }

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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote && TransportItemTags.WRENCHES.contains(player.getHeldItem(hand).getItem())) {
            state = state.with(NORTH_WEST, !state.get(NORTH_WEST));
            world.setBlockState(pos, state, 3);
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
        if (state.get(POWERED)) {
            float speedIncrease = .5f;
            if (state.get(NORTH_WEST)) {
                speedIncrease *= -1;
            }
            Vec3d motion = cart.getMotion();
            if (state.get(SHAPE) == RailShape.NORTH_SOUTH) {
                cart.setMotion(motion.add(0, 0, speedIncrease));
            } else {
                cart.setMotion(motion.add(speedIncrease, 0, 0));
            }
        } else {
            cart.setMotion(Vec3d.ZERO);
        }
    }

    @Override
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
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockState = super.getDefaultState();
        Direction direction = context.getPlacementHorizontalFacing();
        switch (direction) {
            case NORTH:
            case SOUTH:
                blockState = blockState.with(SHAPE, RailShape.NORTH_SOUTH);
                break;
            case WEST:
            case EAST:
                blockState = blockState.with(SHAPE, RailShape.EAST_WEST);
                break;
        }
        switch (direction) {
            case EAST:
            case SOUTH:
                blockState = blockState.with(NORTH_WEST, false);
                break;
            case WEST:
            case NORTH:
                blockState = blockState.with(NORTH_WEST, true);
                break;
        }
        return blockState;
    }
}
