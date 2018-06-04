package xyz.brassgoggledcoders.transport.tracks.block;

import com.teamacronymcoders.base.blocks.IHasBlockStateMapper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import xyz.brassgoggledcoders.transport.library.block.track.BlockRailFoundation;
import xyz.brassgoggledcoders.transport.library.block.track.RailPredicates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRailDiamondCrossing extends BlockRailFoundation implements IHasBlockStateMapper {
    public static final PropertyEnum<EnumRailDirection> SHAPE =
            PropertyEnum.create("shape", EnumRailDirection.class, RailPredicates.FLAT_STRAIGHT::test);

    public BlockRailDiamondCrossing() {
        super("diamond_crossing");
    }

    @Override
    public EnumRailDirection getRailDirection(IBlockAccess world, BlockPos pos, IBlockState state, @Nullable EntityMinecart cart) {
        if (cart != null) {
            EnumFacing cartFacing = cart.getHorizontalFacing();
            if (cartFacing == EnumFacing.NORTH || cartFacing == EnumFacing.SOUTH) {
                return EnumRailDirection.EAST_WEST;
            }
        }
        return EnumRailDirection.NORTH_SOUTH;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, SHAPE);
    }

    @Nonnull
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }
}
