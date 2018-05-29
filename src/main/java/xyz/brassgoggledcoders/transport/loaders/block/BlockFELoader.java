package xyz.brassgoggledcoders.transport.loaders.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.library.block.loader.BlockLoaderBase;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityFELoader;

import javax.annotation.Nonnull;

public class BlockFELoader extends BlockLoaderBase<TileEntityFELoader> {
    public BlockFELoader() {
        super("fe");
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileEntityFELoader();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityFELoader.class;
    }
}
