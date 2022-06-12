package xyz.brassgoggledcoders.transport.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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
        ItemStack found = ItemStack.EMPTY;
        int tried = 0;
        while (found.isEmpty() && tried++ < pattern.getSlots()) {
            int slot = position + tried;
            if (slot >= pattern.getSlots()) {
                slot -= pattern.getSlots();
            }
            found = pattern.getStackInSlot(slot);
        }
        if (simulate) {
            return found;
        } else {
            if (found.getCount() > 1) {
                return found;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public void nextPosition() {
        int tries = 0;
        ItemStack nextFound;
        do {
            position++;
            if (position >= pattern.getSlots()) {
                this.position = 0;
            }
            nextFound = pattern.getStackInSlot(position);
        } while (++tries < pattern.getSlots() && nextFound.isEmpty());

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

    public int getPosition() {
        return position;
    }

    public static PatternedRailProvider fromTag(CompoundTag compoundTag) {
        PatternedRailProvider patternedRailProvider = new PatternedRailProvider();
        patternedRailProvider.pattern.deserializeNBT(compoundTag.getCompound("Pattern"));
        patternedRailProvider.position = compoundTag.getInt("Position");
        return patternedRailProvider;
    }
}
