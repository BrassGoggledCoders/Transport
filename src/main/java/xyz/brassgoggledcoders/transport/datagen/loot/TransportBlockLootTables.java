package xyz.brassgoggledcoders.transport.datagen.loot;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyBlockState;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.BuoyBlock;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TransportBlockLootTables extends BlockLootTables {

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS
                .getValues()
                .stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Transport.ID))
                        .isPresent()
                ).collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        this.registerLootTable(TransportBlocks.ELEVATOR_SWITCH_SUPPORT.get(), LootTable.builder());
        this.registerLootTable(TransportBlocks.MODULE_CONFIGURATOR.getBlock(), block -> LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .acceptCondition(SurvivesExplosion.builder())
                        .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                .replaceOperation("modularInventory", "BlockEntityTag.modularInventory")
                        )
                        .addEntry(ItemLootEntry.builder(block))
                )
        );
        this.registerLootTable(TransportBlocks.SCAFFOLDING_SLAB_BLOCK.getBlock(), block -> LootTable.builder()
                .addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .acceptCondition(BlockStateProperty.builder(block)
                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                        .withBoolProp(ScaffoldingSlabBlock.RAILED, false)))
                        .addEntry(withExplosionDecay(block, ItemLootEntry.builder(block)
                                .acceptFunction(SetCount.builder(ConstantRange.of(2))
                                        .acceptCondition(BlockStateProperty.builder(block)
                                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                        .withProp(SlabBlock.TYPE, SlabType.DOUBLE)
                                                )
                                        )
                                ))
                        )
                )
        );
    }
}
