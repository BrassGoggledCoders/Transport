package xyz.brassgoggledcoders.transport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BuoyBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public BuoyBlock() {
        this(Properties.create(Material.IRON)
                .notSolid()
                .setLightLevel(blockState -> blockState.get(HALF) == DoubleBlockHalf.UPPER ? 5 : 0)
        );
        this.setDefaultState(this.getStateContainer().getBaseState().with(HALF, DoubleBlockHalf.LOWER));
    }

    public BuoyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        if (context.getWorld().getBlockState(context.getPos().up()).isReplaceable(context)) {
            return this.getDefaultState();
        } else {
            return null;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState updatePostPlacement(BlockState blockState, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        return !blockState.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() :
                super.updatePostPlacement(blockState, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean isValidPosition(BlockState blockState, IWorldReader worldReader, BlockPos blockPos) {
        BlockPos downBlockPos = blockPos.down();
        if (blockState.get(HALF) == DoubleBlockHalf.LOWER) {
            return worldReader.getFluidState(downBlockPos).getFluid().isIn(FluidTags.WATER);
        } else {
            return worldReader.getBlockState(downBlockPos).getBlock() == this;
        }
    }
}
