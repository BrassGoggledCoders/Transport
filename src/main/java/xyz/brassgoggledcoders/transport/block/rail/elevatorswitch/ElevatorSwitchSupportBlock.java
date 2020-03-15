package xyz.brassgoggledcoders.transport.block.rail.elevatorswitch;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ElevatorSwitchSupportBlock extends Block {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ElevatorSwitchSupportBlock() {
        this(Properties.create(Material.MISCELLANEOUS, MaterialColor.SAND)
                .doesNotBlockMovement()
                .sound(SoundType.SCAFFOLDING)
                .variableOpacity());
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    public ElevatorSwitchSupportBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, net.minecraft.entity.LivingEntity entity) {
        return true;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.updateState(world, pos);
    }

    private void updateState(World world, BlockPos pos) {
        boolean powered = world.isBlockPowered(pos);
        BlockState blockState = world.getBlockState(pos.up());
        if (blockState.getBlock() == TransportBlocks.ELEVATOR_SWITCH_RAIL.getBlock()) {
            if (!powered) {
                BlockState railState = ElevatorSwitchRailBlock.oppositeAscend(blockState)
                        .with(ElevatorSwitchRailBlock.TOP, false);
                world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
                world.setBlockState(pos, railState);
            }
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}
