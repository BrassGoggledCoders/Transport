package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class DiamondCrossingRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class,
            railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST);

    public DiamondCrossingRailBlock() {
        this(Block.Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
    }

    public DiamondCrossingRailBlock(Block.Properties properties) {
        super(true, properties);
        this.setDefaultState(this.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public RailShape getRailDirection(BlockState state, IBlockReader world, BlockPos pos, @Nullable AbstractMinecartEntity cart) {
        if (cart != null) {
            Direction cartFacing = cart.getHorizontalFacing();
            if (cartFacing == Direction.NORTH || cartFacing == Direction.SOUTH) {
                return RailShape.EAST_WEST;
            }
        }
        return RailShape.NORTH_SOUTH;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }
}
