package xyz.brassgoggledcoders.transport.loot.entry;

import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public interface ILootDrop {
    void onLootDrop(Consumer<ItemStack> itemStackConsumer);
}
