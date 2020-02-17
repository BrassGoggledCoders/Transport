package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.teamacronymcoders.base.capability.item.ItemHandlerDirectional;
import com.teamacronymcoders.base.capability.item.ItemStackQueue;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Optional;

public class TileEntityItemLoader extends TileEntityLoaderBase<IItemHandler> {
    private final ItemStackQueue itemStackQueue;
    private final IItemHandler itemHandlerInput;
    private final IItemHandler itemHandlerOutput;

    public TileEntityItemLoader() {
        itemStackQueue = new ItemStackQueue();

        itemHandlerInput = new ItemHandlerDirectional(itemStackQueue, true);
        itemHandlerOutput = new ItemHandlerDirectional(itemStackQueue, false);
    }

    @Override
    protected void readCapability(NBTTagCompound data) {
        itemStackQueue.deserializeNBT(data.getCompoundTag("inventory"));
    }

    @Override
    protected void writeCapability(NBTTagCompound data) {
        data.setTag("inventory", itemStackQueue.serializeNBT());
    }

    @Override
    public Capability<IItemHandler> getCapabilityType() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public IItemHandler getInternalCapability() {
        return itemStackQueue;
    }

    @Override
    public IItemHandler getOutputCapability() {
        return itemHandlerOutput;
    }

    @Override
    public IItemHandler getInputCapability() {
        return itemHandlerInput;
    }

    @Override
    protected boolean transfer(IItemHandler from, IItemHandler to) {
        for (int fromIndex = 0; fromIndex < from.getSlots(); fromIndex++)  {
            ItemStack itemStackSimPulled = from.extractItem(fromIndex, 64, true);
            if (!itemStackSimPulled.isEmpty()) {
                ItemStack itemStackSimPushed = ItemHandlerHelper.insertItem(to, itemStackSimPulled, true);
                if (itemStackSimPushed.isEmpty()) {
                    ItemHandlerHelper.insertItem(to, from.extractItem(fromIndex, 64, false), false);
                    return true;
                }
            }
        }
        return false;
    }

    public void dropItems() {
        Optional<ItemStack> itemStack = this.itemStackQueue.pull();
        while (itemStack.isPresent()) {
            InventoryHelper.spawnItemStack(this.getWorld(), this.getPos().getX(), this.getPos().getY(),
                    this.getPos().getZ(), itemStack.get());
            itemStack = this.itemStackQueue.pull();
        }
    }
}
