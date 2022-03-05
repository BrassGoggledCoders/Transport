package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.DumpRailBlock;
import xyz.brassgoggledcoders.transport.blockentity.DumpRailBlockEntity;

public class TransportBlocks {

    public static final BlockEntry<DumpRailBlock<IItemHandler>> ITEM_DUMP_RAIL = Transport.getRegistrate()
            .object("item_dump_rail")
            .block(DumpRailBlock::itemDumpRail)
            .properties(BlockBehaviour.Properties::noCollission)
            .addLayer(() -> RenderType::cutout)
            .blockstate((TransportBlocks::straightRailBlockState))
            .tag(BlockTags.RAILS)
            .simpleBlockEntity(DumpRailBlockEntity::new)
            .item()
            .model((context, provider) -> provider.generated(context, provider.modLoc("block/rail/item_dump_rail")))
            .build()
            .register();

    public static <T extends BaseRailBlock> void straightRailBlockState(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider) {
        ModelFile flatRail = provider.models().getBuilder("block/" + context.getName() + "_flat")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile raisedRail = provider.models().getBuilder("block/" + context.getName() + "_raised")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/template_rail_raised_ne"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        provider.getVariantBuilder(context.get())
                .forAllStatesExcept(blockState -> {
                    RailShape railShape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
                    ConfiguredModel.Builder<?> modelBuilder = ConfiguredModel.builder();
                    if (railShape.isAscending()) {
                        modelBuilder.modelFile(raisedRail);
                    } else {
                        modelBuilder.modelFile(flatRail);
                    }

                    modelBuilder.rotationY(switch (railShape) {
                        case EAST_WEST, ASCENDING_EAST -> 90;
                        case ASCENDING_SOUTH -> 180;
                        case ASCENDING_WEST -> 270;
                        default -> 0;
                    });

                    return modelBuilder.build();
                }, BlockStateProperties.WATERLOGGED, BlockStateProperties.POWERED);
    }

    public static void setup() {

    }
}
