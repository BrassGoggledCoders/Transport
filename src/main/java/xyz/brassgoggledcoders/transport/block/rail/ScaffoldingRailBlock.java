package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ScaffoldingRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class);

    public ScaffoldingRailBlock() {
        this(Block.Properties.create(Material.MISCELLANEOUS)
                .notSolid()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
    }

    public ScaffoldingRailBlock(Properties properties) {
        super(false, properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
    }

    @Override
    @Nonnull
    public IProperty<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean isValidPosition(BlockState state, @Nonnull IWorldReader world, BlockPos pos) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!world.isRemote) {
            this.updateState(state, world, pos, block);
        }
    }
}
