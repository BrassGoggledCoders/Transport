package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.component.inventory.MultiInventoryComponent;
import net.minecraft.tileentity.TileEntityType;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

public class ItemLoaderTileEntity extends BasicLoaderTileEntity<MultiInventoryComponent<ItemLoaderTileEntity>> {

    public ItemLoaderTileEntity() {
        this(TransportBlocks.ITEM_LOADER_TILE_ENTITY.get());
    }

    public <T extends ItemLoaderTileEntity> ItemLoaderTileEntity(TileEntityType<T> tileEntityType) {
        super(tileEntityType, new MultiInventoryComponent<>());
    }
}
