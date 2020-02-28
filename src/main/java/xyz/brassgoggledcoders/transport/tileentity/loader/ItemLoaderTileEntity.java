package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.capability.ItemHandlerDirectional;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class ItemLoaderTileEntity extends BasicLoaderTileEntity<IItemHandler>
        implements IScreenAddonProvider {
    private final InventoryComponent<ItemLoaderTileEntity> inventoryComponent;
    private final LazyOptional<IItemHandler> internalLazyOptional;

    public ItemLoaderTileEntity() {
        this(TransportBlocks.ITEM_LOADER.getTileEntityType());
    }

    public <T extends ItemLoaderTileEntity> ItemLoaderTileEntity(TileEntityType<T> tileEntityType) {
        super(tileEntityType, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        this.inventoryComponent = new InventoryComponent<>("inventory", 30, 30, 5);
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("inventory", this.inventoryComponent.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.inventoryComponent.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return this.inventoryComponent.getScreenAddons();
    }

    @Override
    public List<Slot> getSlots() {
        List<Slot> slots = Lists.newArrayList();
        int i = 0;
        for (int y = 0; y < inventoryComponent.getYSize(); ++y) {
            for (int x = 0; x < inventoryComponent.getXSize(); ++x) {
                slots.add(new SlotItemHandler(inventoryComponent, i,
                        inventoryComponent.getXPos() + inventoryComponent.getSlotPosition().apply(i).getLeft(),
                        inventoryComponent.getYPos() + inventoryComponent.getSlotPosition().apply(i).getRight()
                ));
                ++i;
            }
        }
        return slots;
    }
}
