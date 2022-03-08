package xyz.brassgoggledcoders.transport.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import xyz.brassgoggledcoders.transport.block.rail.OneWayBoosterRailBlock;

public class RailModelHelper {
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

    public static void straightPoweredInvertedRailBlockState(DataGenContext<Block, OneWayBoosterRailBlock> context,
                                                             RegistrateBlockstateProvider provider) {
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

        ModelFile raisedRailUnpoweredInverted = provider.models().getBuilder("block/" + context.getName() + "_raised_inverted")
                .parent(provider.models()
                        .getExistingFile(provider.modLoc("block/template_rail_raised_ne_inverted"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile raisedRailPoweredInverted = provider.models().getBuilder("block/" + context.getName() + "_raised_inverted_powered")
                .parent(provider.models()
                        .getExistingFile(provider.modLoc("block/template_rail_raised_ne_inverted"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_powered"));

        provider.getVariantBuilder(context.get())
                .forAllStatesExcept(blockState -> {
                    RailShape railShape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
                    boolean powered = blockState.getValue(BlockStateProperties.POWERED);
                    boolean inverted = blockState.getValue(BlockStateProperties.INVERTED);
                    ConfiguredModel.Builder<?> modelBuilder = ConfiguredModel.builder();
                    if (railShape.isAscending() && inverted) {
                        modelBuilder.modelFile(powered ? raisedRailPoweredInverted : raisedRailUnpoweredInverted);
                    } else if (railShape.isAscending()) {
                        modelBuilder.modelFile(powered ? raisedRailPowered : raisedRailUnpowered);
                    } else {
                        modelBuilder.modelFile(powered ? flatRailPowered : flatRailUnpowered);
                    }

                    modelBuilder.rotationY(switch (railShape) {
                        case ASCENDING_EAST -> 90;
                        case ASCENDING_SOUTH -> 180;
                        case ASCENDING_WEST -> 270;
                        case EAST_WEST -> inverted ? 90 : 270;
                        case NORTH_SOUTH -> inverted ? 180 : 0;
                        default -> 0;
                    });

                    return modelBuilder.build();
                }, BlockStateProperties.WATERLOGGED);
    }
}
