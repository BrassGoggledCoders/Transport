package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.DumpRailBlock;
import xyz.brassgoggledcoders.transport.blockentity.DumpRailBlockEntity;

import javax.annotation.Nonnull;

public class TransportBlocks {

    public static final BlockEntry<DumpRailBlock<IItemHandler>> ITEM_DUMP_RAIL = Transport.getRegistrate()
            .object("item_dump_rail")
            .block(DumpRailBlock::itemDumpRail)
            .transform(TransportBlocks::defaultRail)
            .blockstate((TransportBlocks::straightPoweredRailBlockState))
            .transform(TransportBlocks::defaultRailItem)
            .register();

    public static final RegistryEntry<BlockEntityType<DumpRailBlockEntity>> DUMP_RAIL_BLOCK_ENTITY = Transport.getRegistrate()
            .blockEntity(DumpRailBlockEntity::new)
            .validBlock(ITEM_DUMP_RAIL)
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

    public static <T extends BaseRailBlock> void straightPoweredRailBlockState(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider) {
        ModelFile flatRailUnpowered = provider.models().getBuilder("block/" + context.getName() + "_flat")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile flatRailPowered = provider.models().getBuilder("block/" + context.getName() + "_flat_powered")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_powered"));

        ModelFile raisedRailUnpowered = provider.models().getBuilder("block/" + context.getName() + "_raised")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/template_rail_raised_ne"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile raisedRailPowered = provider.models().getBuilder("block/" + context.getName() + "_raised_powered")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/template_rail_raised_ne"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_powered"));

        provider.getVariantBuilder(context.get())
                .forAllStatesExcept(blockState -> {
                    RailShape railShape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
                    boolean powered = blockState.getValue(BlockStateProperties.POWERED);
                    ConfiguredModel.Builder<?> modelBuilder = ConfiguredModel.builder();
                    if (railShape.isAscending()) {
                        modelBuilder.modelFile(powered ? raisedRailPowered : raisedRailUnpowered);
                    } else {
                        modelBuilder.modelFile(powered ? flatRailPowered : flatRailUnpowered);
                    }

                    modelBuilder.rotationY(switch (railShape) {
                        case EAST_WEST, ASCENDING_EAST -> 90;
                        case ASCENDING_SOUTH -> 180;
                        case ASCENDING_WEST -> 270;
                        default -> 0;
                    });

                    return modelBuilder.build();
                }, BlockStateProperties.WATERLOGGED);
    }

    public static void setup() {

    }
}
