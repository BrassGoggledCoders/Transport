package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ScaffoldingRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class);

    public ScaffoldingRailBlock(Properties properties) {
        super(false, properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!isMoving && world.isAirBlock(pos.down())) {
            world.setBlockState(pos.down(), TransportBlocks.SCAFFOLDING_SLAB_BLOCK.get().getDefaultState()
                    .with(SlabBlock.TYPE, SlabType.TOP)
                    .with(ScaffoldingSlabBlock.RAILED, true));
        }
    }

    @Override
    public boolean isValidPosition(@Nonnull BlockState state, @Nonnull IWorldReader world, BlockPos pos) {
        return hasSolidSideOnTop(world, pos.down()) || world.isAirBlock(pos.down());
    }
}
