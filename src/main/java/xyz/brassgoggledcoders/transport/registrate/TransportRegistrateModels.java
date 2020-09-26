package xyz.brassgoggledcoders.transport.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.properties.RailShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.NonNullLazy;

public class TransportRegistrateModels {

    public static NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> railItem() {
        return (context, provider) -> provider.generated(context, provider.modLoc("block/rail/" + context.getName()));
    }

    public static NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> railItem(String name) {
        return (context, provider) -> provider.generated(context, provider.modLoc("block/rail/" + name));
    }

    @SuppressWarnings("deprecation")
    public static <T extends AbstractRailBlock> void rail(DataGenContext<Block, T> context,
                                                          RegistrateBlockstateProvider provider) {
        NonNullLazy<ModelFile> straightFlat = NonNullLazy.concurrentOf(
                () -> provider.models().withExistingParent(context.getName(), provider.mcLoc("block/rail_flat"))
                        .texture("rail", provider.modLoc("block/rail/" + context.getName())));

        provider.getVariantBuilder(context.get())
                .forAllStates(blockState -> {
                    RailShape railShape = blockState.get(context.get().getShapeProperty());
                    ConfiguredModel.Builder<?> modelBuilder = ConfiguredModel.builder();
                    switch (railShape) {
                        case NORTH_SOUTH:
                            modelBuilder.modelFile(straightFlat.get());
                            break;
                        case EAST_WEST:
                            modelBuilder.modelFile(straightFlat.get())
                                    .rotationY(90);
                            break;
                        default:
                            break;
                    }

                    return modelBuilder.build();
                });


    }
}
