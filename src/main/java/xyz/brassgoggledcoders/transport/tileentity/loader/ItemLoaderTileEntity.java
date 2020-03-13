package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.transport.capability.InventoryPlusComponent;
import xyz.brassgoggledcoders.transport.capability.ItemHandlerDirectional;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.Collections;
import java.util.List;

public class ItemLoaderTileEntity extends BasicLoaderTileEntity<IItemHandler>
        implements IScreenAddonProvider {
    private final InventoryPlusComponent<ItemLoaderTileEntity> inventoryComponent;
    private final LazyOptional<IItemHandler> internalLazyOptional;

    public ItemLoaderTileEntity() {
        this(TransportBlocks.ITEM_LOADER.getTileEntityType());
    }

    public <T extends ItemLoaderTileEntity> ItemLoaderTileEntity(TileEntityType<T> tileEntityType) {
        super(tileEntityType, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        this.inventoryComponent = new InventoryPlusComponent<>("inventory", 44, 35, 5);
        this.internalLazyOptional = LazyOptional.of(() -> inventoryComponent);
    }

    @Override
    protected void transfer(IItemHandler from, IItemHandler to) {
        for (int fromIndex = 0; fromIndex < from.getSlots(); fromIndex++) {
            ItemStack itemStackSimPulled = from.extractItem(fromIndex, 64, true);
            if (!itemStackSimPulled.isEmpty()) {
                ItemStack itemStackSimPushed = ItemHandlerHelper.insertItem(to, itemStackSimPulled, true);
                if (itemStackSimPushed.isEmpty()) {
                    ItemHandlerHelper.insertItem(to, from.extractItem(fromIndex, 64, false), false);
                    break;
                }
            }
        }
    }

    @Override
    protected LazyOptional<IItemHandler> getInternalCAP() {
        return this.internalLazyOptional;
    }

    @Override
    protected LazyOptional<IItemHandler> createOutputCAP() {
        return LazyOptional.of(() -> new ItemHandlerDirectional(this.inventoryComponent, false));
    }

    @Override
    protected LazyOptional<IItemHandler> createInputCAP() {
        return LazyOptional.of(() -> new ItemHandlerDirectional(this.inventoryComponent, true));
    }

    @Override
    protected CompoundNBT serializeCap() {
        return inventoryComponent.serializeNBT();
    }

    @Override
    protected void deserializeCap(CompoundNBT compoundNBT) {
        inventoryComponent.deserializeNBT(compoundNBT);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return this.inventoryComponent.getScreenAddons();
    }

    @Override
    public List<IContainerAddon> getContainerAddons() {
        return Collections.singletonList(inventoryComponent);
    }
}
