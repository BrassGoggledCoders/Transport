package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.tileentity.TileEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;

public class ItemLoaderBlock extends LoaderBlock {
    public ItemLoaderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createLoaderTileEntity() {
        return new ItemLoaderTileEntity(TransportBlocks.ITEM_LOADER_TILE_ENTITY.get());
    }
}
