package xyz.brassgoggledcoders.transport.loaders.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.library.block.loader.BlockLoaderBase;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityItemLoader;

import javax.annotation.Nonnull;

public class BlockItemLoader extends BlockLoaderBase<TileEntityItemLoader> {
    public BlockItemLoader() {
        super("item");
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileEntityItemLoader();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityItemLoader.class;
    }
}
