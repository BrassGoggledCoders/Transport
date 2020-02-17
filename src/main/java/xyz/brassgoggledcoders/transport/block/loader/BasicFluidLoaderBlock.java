package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.tileentity.loader.TileEntityFluidLoader;

import javax.annotation.Nonnull;

public class BasicFluidLoaderBlock extends BasicLoaderBlock<TileEntityFluidLoader> {
    public BasicFluidLoaderBlock() {
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
