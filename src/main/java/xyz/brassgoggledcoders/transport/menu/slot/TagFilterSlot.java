package xyz.brassgoggledcoders.transport.menu.slot;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TagFilterSlot extends Slot {
    private final TagKey<Item> tagKey;
    public TagFilterSlot(Container pContainer, int pIndex, int pX, int pY, TagKey<Item> tagKey) {
        super(pContainer, pIndex, pX, pY);
        this.tagKey = tagKey;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return pStack.is(tagKey);
    }
}
