package xyz.brassgoggledcoders.transport.content;

import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.util.registry.Registry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.loot.entry.LootDropLootEntrySerializer;

public class TransportLoots {
    public static final LootPoolEntryType LOOT_DROP_ENTRY_TYPE = Registry.register(
            Registry.LOOT_POOL_ENTRY_TYPE,
            Transport.rl("loot_drop"),
            new LootPoolEntryType(new LootDropLootEntrySerializer())
    );

    public static void setup() {

    }
}
