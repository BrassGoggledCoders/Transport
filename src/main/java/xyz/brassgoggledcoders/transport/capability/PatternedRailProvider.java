package xyz.brassgoggledcoders.transport.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;

public class PatternedRailProvider implements IRailProvider {
    private final ItemStackHandler pattern = new ItemStackHandler(9);
    private int position = 0;

    @Override
    @NotNull
    public ItemStack findNext(IItemHandler searchable, boolean simulate) {
        if (simulate) {
            ItemStack found = ItemStack.EMPTY;
            int tried = 0;
            while (found.isEmpty() && tried++ < pattern.getSlots()) {
                int slot = position + tried;
                if (slot >= pattern.getSlots()) {
                    slot -= pattern.getSlots();
                }
                found = pattern.getStackInSlot(slot);
            }
            return found;
        } else {
            return new ItemStack(Items.RAIL);
        }
    }

    public IItemHandlerModifiable getPattern() {
        return pattern;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("Pattern", pattern.serializeNBT());
        tag.putInt("Position", position);
        return tag;
    }

    public static PatternedRailProvider fromTag(CompoundTag compoundTag) {
        PatternedRailProvider patternedRailProvider = new PatternedRailProvider();
        patternedRailProvider.pattern.deserializeNBT(compoundTag.getCompound("Pattern"));
        patternedRailProvider.position = compoundTag.getInt("Position");
        return patternedRailProvider;
    }
}
