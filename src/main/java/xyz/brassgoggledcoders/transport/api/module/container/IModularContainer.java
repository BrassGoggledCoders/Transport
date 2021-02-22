package xyz.brassgoggledcoders.transport.api.module.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;

public interface IModularContainer {

    void putSlot(Slot slot);

    PlayerInventory getPlayerInventory();

    Container asContainer();

    boolean attemptMergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection);

    LazyOptional<IModularEntity> getModularEntity();

    ModuleTab<?> getActiveTab();
}
