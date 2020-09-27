package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportBlockStateProperties;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.rail.YardRailTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class YardRailBlock extends AbstractRailBlock {
    public static final Property<RailShape> SHAPE = TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE;

    public YardRailBlock(Properties builder) {
        super(true, builder);
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof YardRailTileEntity) {
            ((YardRailTileEntity) tileEntity).onMinecartPass(cart);
        }
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new YardRailTileEntity(TransportBlocks.YARD_RAIL_TILE_ENTITY.get());
    }
}
