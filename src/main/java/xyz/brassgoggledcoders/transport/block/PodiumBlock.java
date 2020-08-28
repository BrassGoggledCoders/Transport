package xyz.brassgoggledcoders.transport.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class PodiumBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_ITEM = BlockStateProperties.HAS_BOOK;
    public static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final VoxelShape POST_SHAPE = Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
    public static final VoxelShape COMMON_SHAPE = VoxelShapes.or(BASE_SHAPE, POST_SHAPE);
    public static final VoxelShape TOP_PLATE_SHAPE = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public static final VoxelShape COLLISION_SHAPE = VoxelShapes.or(COMMON_SHAPE, TOP_PLATE_SHAPE);
    public static final VoxelShape WEST_SHAPE = VoxelShapes.or(Block.makeCuboidShape(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), Block.makeCuboidShape(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), Block.makeCuboidShape(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D), COMMON_SHAPE);
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.makeCuboidShape(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.makeCuboidShape(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), COMMON_SHAPE);
    public static final VoxelShape EAST_SHAPE = VoxelShapes.or(Block.makeCuboidShape(15.0D, 10.0D, 0.0D, 10.666667D, 14.0D, 16.0D), Block.makeCuboidShape(10.666667D, 12.0D, 0.0D, 6.333333D, 16.0D, 16.0D), Block.makeCuboidShape(6.333333D, 14.0D, 0.0D, 2.0D, 18.0D, 16.0D), COMMON_SHAPE);
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 10.0D, 15.0D, 16.0D, 14.0D, 10.666667D), Block.makeCuboidShape(0.0D, 12.0D, 10.666667D, 16.0D, 16.0D, 6.333333D), Block.makeCuboidShape(0.0D, 14.0D, 6.333333D, 16.0D, 18.0D, 2.0D), COMMON_SHAPE);

    public PodiumBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.NORTH)
                .with(HAS_ITEM, false)
        );
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getRenderShape(BlockState state, IBlockReader world, BlockPos pos) {
        return COMMON_SHAPE;
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        ItemStack itemstack = context.getItem();
        CompoundNBT compoundnbt = itemstack.getTag();
        PlayerEntity playerentity = context.getPlayer();
        boolean flag = false;
        if (!world.isRemote && playerentity != null && compoundnbt != null && playerentity.canUseCommandBlock() && compoundnbt.contains("BlockEntityTag")) {
            CompoundNBT blockEntityTag = compoundnbt.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("displayItemStack")) {
                flag = true;
            }
        }

        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(HAS_ITEM, flag);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return COMMON_SHAPE;
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_ITEM);
    }


    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean allowsMovement(BlockState state, IBlockReader blockReader, BlockPos pos, PathType type) {
        return false;
    }
}
