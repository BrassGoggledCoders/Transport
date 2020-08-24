package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ItemHandlerDirectional;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class ItemLoaderTileEntity extends BasicLoaderTileEntity<IItemHandler>
        implements IScreenAddonProvider {
    private final InventoryComponent<ItemLoaderTileEntity> inventoryComponent;
    private final LazyOptional<IItemHandler> internalLazyOptional;

    public ItemLoaderTileEntity(TileEntityType<ItemLoaderTileEntity> tileEntityType) {
        super(tileEntityType, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        this.inventoryComponent = new InventoryComponent<>("inventory", 44, 35, 5);
        this.internalLazyOptional = LazyOptional.of(() -> inventoryComponent);
    }

    @Override
    protected void transfer(IItemHandler from, IItemHandler to) {
        int moved = 0;
        int slotNumber = 0;
        do {
            ItemStack itemStack = from.extractItem(slotNumber, 16, true);
            if (!itemStack.isEmpty()) {
                if (ItemHandlerHelper.insertItem(to, itemStack, true).isEmpty()) {
                    ItemStack movedItemStack = from.extractItem(slotNumber, 16, false);
                    moved += movedItemStack.getCount();
                    ItemHandlerHelper.insertItem(to, movedItemStack, false);
                }
            }
            slotNumber++;
        } while (slotNumber < from.getSlots() && moved < 32);
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
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return inventoryComponent.getContainerAddons();
    }
}
