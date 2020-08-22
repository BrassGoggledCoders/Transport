package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.tileentity.TileEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;

public class FluidLoaderBlock extends LoaderBlock {
    public FluidLoaderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createLoaderTileEntity() {
        return new FluidLoaderTileEntity(TransportBlocks.FLUID_LOADER_TILE_ENTITY.get());
    }
}
