package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.tileentity.loader.TileEntityItemLoader;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BasicItemLoaderBlock extends BasicLoaderBlock<TileEntityItemLoader> {
    public BasicItemLoaderBlock() {
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

    @Override
    @ParametersAreNonnullByDefault
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            this.getTileEntity(world, pos).ifPresent(TileEntityItemLoader::dropItems);
        }

        super.breakBlock(world, pos, state);
    }
}
