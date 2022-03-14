package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.rail.DumpRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.OneWayBoosterRailBlock;
import xyz.brassgoggledcoders.transport.block.storage.BlockCapabilityStorage;
import xyz.brassgoggledcoders.transport.blockentity.DumpRailBlockEntity;
import xyz.brassgoggledcoders.transport.blockentity.storage.BlockEntityFluidStorage;
import xyz.brassgoggledcoders.transport.util.BlockModelHelper;

import javax.annotation.Nonnull;

public class TransportBlocks {

    public static final BlockEntry<DumpRailBlock<IItemHandler>> ITEM_DUMP_RAIL = Transport.getRegistrate()
            .object("item_dump_rail")
            .block(DumpRailBlock::itemDumpRail)
            .transform(TransportBlocks::defaultRail)
            .blockstate(BlockModelHelper::straightPoweredRailBlockState)
            .transform(TransportBlocks::defaultRailItem)
            .register();

    public static final RegistryEntry<BlockEntityType<DumpRailBlockEntity>> DUMP_RAIL_BLOCK_ENTITY = Transport.getRegistrate()
            .object("dump_rail")
            .blockEntity(DumpRailBlockEntity::new)
            .validBlock(ITEM_DUMP_RAIL)
            .register();

    public static final BlockEntry<OneWayBoosterRailBlock> ONE_WAY_BOOSTER_RAIL = Transport.getRegistrate()
            .object("one_way_booster_rail")
            .block(OneWayBoosterRailBlock::new)
            .transform(TransportBlocks::defaultRail)
            .blockstate(BlockModelHelper::straightPoweredInvertedRailBlockState)
            .transform(TransportBlocks::defaultRailItem)
            .register();


    public static final BlockEntry<BlockCapabilityStorage<BlockEntityFluidStorage>> FLUID_STORAGE = Transport.getRegistrate()
            .object("fluid_storage")
            .block(properties -> new BlockCapabilityStorage<>(properties, BlockEntityFluidStorage::new))
            .blockstate(BlockModelHelper::storageBlock)
            .item()
            .build()
            .blockEntity(BlockEntityFluidStorage::new)
            .build()
            .register();

    @Nonnull
    public static <T extends BaseRailBlock> BlockBuilder<T, Registrate> defaultRail(BlockBuilder<T, Registrate> builder) {
        return builder.initialProperties(Material.DECORATION)
                .properties(properties -> properties.noCollission()
                        .strength(0.7F)
                        .sound(SoundType.METAL)
                )
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.RAILS);
    }

    @Nonnull
    public static <T extends BaseRailBlock> BlockBuilder<T, Registrate> defaultRailItem(BlockBuilder<T, Registrate> builder) {
        return builder.item()
                .model((context, provider) -> provider.generated(context, provider.modLoc("block/rail/" + context.getName())))
                .tag(ItemTags.RAILS)
                .build();
    }

    public static void setup() {

    }
}
