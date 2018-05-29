package xyz.brassgoggledcoders.transport.tracks.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.library.block.track.RailPredicates;

import javax.annotation.Nonnull;

public class BlockRailWye extends BlockRailFoundation {
    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.create("shape", EnumRailDirection.class,
            RailPredicates.ALL_CURVES::test);
    public static final PropertyBool SWITCHED = PropertyBool.create("switched");

    public BlockRailWye() {
        super("wye");
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, EnumRailDirection.SOUTH_EAST)
                .withProperty(SWITCHED, false));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.getHeldItem(hand).isEmpty()) {
            if (state.getValue(SWITCHED)) {
                int shapeValue = state.getValue(SHAPE).getMetadata() + 1;
                if (shapeValue > 9) {
                    shapeValue = 6;
                }
                state = state.withProperty(SHAPE, EnumRailDirection.byMetadata(shapeValue));
            }

            state = state.withProperty(SWITCHED, !state.getValue(SWITCHED));
            world.setBlockState(pos, state, 3);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        IBlockState blockState = this.getDefaultState();
        blockState = blockState.withProperty(SHAPE, EnumRailDirection.byMetadata(meta % 4 + 6));
        blockState = blockState.withProperty(SWITCHED, meta > 3);
        return blockState;
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        int meta = blockState.getValue(SHAPE).getMetadata() - 6;
        meta += blockState.getValue(SWITCHED) ? 4 : 0;
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE, SWITCHED);
    }

    @Nonnull
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }
}
