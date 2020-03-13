package xyz.brassgoggledcoders.transport.block.rail.elevatorswitch;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class ElevatorSwitchSupportBlock extends Block {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ElevatorSwitchSupportBlock() {
        this(Properties.create(Material.MISCELLANEOUS, MaterialColor.SAND)
                .doesNotBlockMovement()
                .sound(SoundType.SCAFFOLDING)
                .variableOpacity());
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
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.updateState(worldIn, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.updateState(world, pos);
        }
    }

    private void updateState(World world, BlockPos pos) {
        boolean powered = world.isBlockPowered(pos);
        if (!powered) {
            BlockState railState = world.getBlockState(pos.up());
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.NOTIFY_NEIGHBORS);
            world.setBlockState(pos, ElevatorSwitchRailBlock.oppositeAscend(railState)
                    .with(ElevatorSwitchRailBlock.TOP, false));
        }
    }
}
