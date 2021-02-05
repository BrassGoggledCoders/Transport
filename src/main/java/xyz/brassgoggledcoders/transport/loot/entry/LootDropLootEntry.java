package xyz.brassgoggledcoders.transport.loot.entry;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.brassgoggledcoders.transport.content.TransportLoots;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

public class LootDropLootEntry extends StandaloneLootEntry {
    public LootDropLootEntry(int weightIn, int qualityIn, ILootCondition[] conditionsIn, ILootFunction[] functionsIn) {
        super(weightIn, qualityIn, conditionsIn, functionsIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void func_216154_a(Consumer<ItemStack> stackConsumer, LootContext context) {
        if (context.has(LootParameters.BLOCK_ENTITY)) {
            TileEntity tileEntity = context.get(LootParameters.BLOCK_ENTITY);
            if (tileEntity instanceof ILootDrop) {
                ((ILootDrop) tileEntity).onLootDrop(stackConsumer);
            }
        }
    }

    @Override
    @Nonnull
    public LootPoolEntryType func_230420_a_() {
        return TransportLoots.LOOT_DROP_ENTRY_TYPE;
    }

    public static StandaloneLootEntry.Builder<?> builder() {
        return builder(LootDropLootEntry::new);
    }
}


