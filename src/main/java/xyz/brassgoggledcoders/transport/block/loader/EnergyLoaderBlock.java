package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.tileentity.TileEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;

public class EnergyLoaderBlock extends LoaderBlock {
    public EnergyLoaderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createLoaderTileEntity() {
        return new EnergyLoaderTileEntity(TransportBlocks.ENERGY_LOADER_TILE_ENTITY.get());
    }
}
