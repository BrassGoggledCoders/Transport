package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.MultiInventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

public class ItemLoaderTileEntity extends BasicLoaderTileEntity<IItemHandler, InventoryComponent<ItemLoaderTileEntity>> {

    public ItemLoaderTileEntity() {
        this(TransportBlocks.ITEM_LOADER_TILE_ENTITY.get());
    }

    public <T extends ItemLoaderTileEntity> ItemLoaderTileEntity(TileEntityType<T> tileEntityType) {
        super(tileEntityType, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                new InventoryComponent<>("Inventory", 10, 10, 5));
    }
}
