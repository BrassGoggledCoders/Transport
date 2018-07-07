package xyz.brassgoggledcoders.transport.loaders.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.library.block.loader.BlockLoaderBase;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityFluidLoader;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityMobLoader;

import javax.annotation.Nonnull;

public class BlockMobLoader extends BlockLoaderBase<TileEntityMobLoader> {
    public BlockMobLoader() {
        super("mob");
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileEntityMobLoader();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileEntityMobLoader.class;
    }
}
