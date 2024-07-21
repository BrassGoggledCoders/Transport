package xyz.brassgoggledcoders.transport.capability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;

import java.util.List;
import java.util.stream.IntStream;

public class PatternedRailProvider implements IRailProvider {
    public static final Codec<PatternedRailProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("pattern").forGetter(PatternedRailProvider::getListForCodec),
            Codec.INT.fieldOf("position").forGetter(PatternedRailProvider::getPosition)
    ).apply(instance, PatternedRailProvider::new));

    private final ItemStackHandler pattern;
    private int position;

    public PatternedRailProvider() {
        this(NonNullList.withSize(9, ItemStack.EMPTY), 0);
    }

    public PatternedRailProvider(List<ItemStack> pattern, int position) {
        this.pattern = new ItemStackHandler(9);
        for (int i = 0; i < 9; i++) {
            if (pattern.size() > i) {
                this.pattern.setStackInSlot(i, pattern.get(i));
            }
        }
        this.position = position;
    }

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

    private List<ItemStack> getListForCodec() {
        return IntStream.range(0, this.getPattern().getSlots())
                .mapToObj(this.getPattern()::getStackInSlot)
                .toList();
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
