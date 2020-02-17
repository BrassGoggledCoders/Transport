package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.block.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.tileentity.loader.TileEntityFELoader;

import javax.annotation.Nonnull;

public class BasicFELoaderBlock extends BasicLoaderBlock<TileEntityFELoader> {
    public BasicFELoaderBlock() {
        super("fe");
    }

    @Nonnull
    @Override
    public TileEntity create(@Nonnull BlockState blockState, @Nonnull IWorldReader world) {
        return new TileEntityFELoader();
    }
}
