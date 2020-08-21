package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyBlockState;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;

import java.util.stream.IntStream;

public class TransportRegistrateBlockLootTables extends BlockLootTables {

    public static <T> T withExplosionDecay(IItemProvider item, ILootFunctionConsumer<T> function) {
        return BlockLootTables.withExplosionDecay(item, function);
    }

    public static <T extends Block>  void registerLoader(RegistrateBlockLootTables blockLootTables, T loader) {
        CopyBlockState.Builder copyBlockStateBuilder = CopyBlockState.func_227545_a_(loader);

        IntStream.range(0, Direction.values().length)
                .mapToObj(Direction::byIndex)
                .map(LoaderBlock.PROPERTIES::get)
                .forEach(copyBlockStateBuilder::func_227552_a_);

        blockLootTables.registerLootTable(loader, new LootTable.Builder()
                .addLootPool(LootPool.builder()
                        .acceptFunction(copyBlockStateBuilder)
                        .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                .replaceOperation("capability", "BlockEntityTag.capability"))
                        .rolls(RandomValueRange.of(1, 1))
                        .addEntry(ItemLootEntry.builder(loader))
                )
        );
    }
}
