package xyz.brassgoggledcoders.transport.util;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import xyz.brassgoggledcoders.transport.block.rail.SwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.WyeSwitchRailBlock;

public class BlockModelHelper {
    public static void regularRail(DataGenContext<Block, ? extends BaseRailBlock> context, RegistrateBlockstateProvider provider) {
        ModelFile flatRail = provider.models()
                .getBuilder("block/" + context.getName() + "_flat")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile raisedRail = provider.models()
                .getBuilder("block/" + context.getName() + "_raised")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/template_rail_raised_ne"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile cornerRail = provider.models()
                .getBuilder("block/" + context.getName() + "_corner")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_corner"));

        provider.getVariantBuilder(context.get())
                .forAllStatesExcept(state -> {
                    RailShape railShape = state.getValue(BlockStateProperties.RAIL_SHAPE);

                    ModelFile modelFile;

                    if (railShape.isAscending()) {
                        modelFile = raisedRail;
                    } else if (RailHelper.isRailShapeStraight(railShape)) {
                        modelFile = flatRail;
                    } else {
                        modelFile = cornerRail;
                    }

                    int rotation = switch (railShape) {
                        case ASCENDING_EAST, SOUTH_WEST, EAST_WEST -> 90;
                        case NORTH_WEST, ASCENDING_SOUTH -> 180;
                        case ASCENDING_WEST, NORTH_EAST -> 270;
                        default -> 0;
                    };

                    return ConfiguredModel.builder()
                            .modelFile(modelFile)
                            .rotationY(rotation)
                            .build();
                });
    }

    public static void straightPoweredRailBlockState(DataGenContext<Block, ? extends BaseRailBlock> context, RegistrateBlockstateProvider provider) {
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

    public static void straightPoweredInvertedRailBlockState(DataGenContext<Block, ? extends BaseRailBlock> context,
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

    public static void storageBlock(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider) {
        provider.simpleBlock(context.get(), provider.models()
                .cubeColumn(
                        context.getName(),
                        provider.modLoc("block/storage/" + context.getName() + "_side"),
                        provider.modLoc("block/storage/" + context.getName() + "_end")
                )
        );
    }

    public static void switchRail(DataGenContext<Block, ? extends SwitchRailBlock> context, RegistrateBlockstateProvider provider) {
        ModelFile straightRight = provider.models()
                .getBuilder("block/" + context.getName() + "_straight_right")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_straight_right"));

        ModelFile divergeRight = provider.models()
                .getBuilder("block/" + context.getName() + "_diverge_right")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_diverge_right"));

        ModelFile straightLeft = provider.models()
                .getBuilder("block/" + context.getName() + "_straight_left")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_straight_left"));

        ModelFile divergeLeft = provider.models()
                .getBuilder("block/" + context.getName() + "_diverge_left")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_diverge_left"));

        provider.getVariantBuilder(context.get())
                .forAllStates(state -> {
                    boolean diverge = state.getValue(SwitchRailBlock.DIVERGE);
                    Pair<Boolean, Integer> setup;

                    if (state.getValue(SwitchRailBlock.STRAIGHT_SHAPE) == RailShape.NORTH_SOUTH) {
                        setup = switch (state.getValue(SwitchRailBlock.DIVERGE_SHAPE)) {
                            case SOUTH_WEST -> Pair.of(true, 0);
                            case NORTH_WEST -> Pair.of(false, 180);
                            case NORTH_EAST -> Pair.of(true, 180);
                            default -> Pair.of(false, 0);
                        };
                    } else {
                        setup = switch (state.getValue(SwitchRailBlock.DIVERGE_SHAPE)) {
                            case SOUTH_WEST -> Pair.of(false, 90);
                            case SOUTH_EAST -> Pair.of(true, 270);
                            case NORTH_WEST -> Pair.of(true, 90);
                            case NORTH_EAST -> Pair.of(false, 270);
                            default -> Pair.of(false, 0);
                        };
                    }

                    ModelFile modelFile;

                    if (diverge) {
                        modelFile = setup.getFirst() ? divergeLeft : divergeRight;
                    } else {
                        modelFile = setup.getFirst() ? straightLeft : straightRight;
                    }

                    return ConfiguredModel.builder()
                            .modelFile(modelFile)
                            .rotationY(setup.getSecond())
                            .build();
                });
    }

    public static void wyeSwitchRail(DataGenContext<Block, ? extends WyeSwitchRailBlock> context, RegistrateBlockstateProvider provider) {
        ModelFile straight = provider.models()
                .getBuilder("block/" + context.getName())
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName()));

        ModelFile diverge = provider.models()
                .getBuilder("block/" + context.getName() + "_diverge")
                .parent(provider.models()
                        .getExistingFile(provider.mcLoc("block/rail_flat"))
                )
                .texture("rail", provider.modLoc("block/rail/" + context.getName() + "_diverge"));

        provider.getVariantBuilder(context.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(WyeSwitchRailBlock.DIVERGE) ? diverge : straight)
                        .rotationY(switch (state.getValue(WyeSwitchRailBlock.SHAPE)) {
                            case NORTH_SOUTH -> state.getValue(WyeSwitchRailBlock.INVERTED) ? 270 : 90;
                            case EAST_WEST -> state.getValue(WyeSwitchRailBlock.INVERTED) ? 180 : 0;
                            default -> 0;
                        })
                        .build());
    }
}
