package xyz.brassgoggledcoders.transport.loaders.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.library.block.loader.BlockLoaderBase;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityFELoader;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityFluidLoader;

import javax.annotation.Nonnull;

public class BlockFluidLoader extends BlockLoaderBase<TileEntityFluidLoader> {
    public BlockFluidLoader() {
        super("fluid");
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileEntityFluidLoader();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityFluidLoader.class;
    }
}
