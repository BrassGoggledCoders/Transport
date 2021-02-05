package xyz.brassgoggledcoders.transport.loot.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class LootDropLootEntrySerializer extends StandaloneLootEntry.Serializer<LootDropLootEntry> {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected LootDropLootEntry deserialize(JsonObject object, JsonDeserializationContext context, int weight,
                                            int quality, ILootCondition[] conditions, ILootFunction[] functions) {
        return new LootDropLootEntry(
                weight,
                quality,
                conditions,
                functions
        );
    }
}
