package xyz.brassgoggledcoders.transport.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.block.BuoyBlock;
import xyz.brassgoggledcoders.transport.block.ModuleConfiguratorBlock;
import xyz.brassgoggledcoders.transport.block.ScaffoldingSlabBlock;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.block.rail.BumperRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.DiamondCrossingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.HoldingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.ScaffoldingRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.elevatorswitch.ElevatorSwitchSupportBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.SwitchRailBlock;
import xyz.brassgoggledcoders.transport.block.rail.turnout.WyeSwitchRailBlock;
import xyz.brassgoggledcoders.transport.item.BuoyBlockItem;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.EnergyLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.FluidLoaderTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TransportBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Transport.ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Transport.ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Transport.ID);

    //region Rails
    public static final BlockEntry<HoldingRailBlock> HOLDING_RAIL = Transport.getRegistrate()
            .object("holding_rail")
            .block(Material.MISCELLANEOUS, HoldingRailBlock::new)
            .properties(railProperties())
            .lang("Holding Rail")
            .tag(BlockTags.RAILS)
            .item()
            .group(Transport::getItemGroup)
            .recipe(((context, recipeProvider) ->
                    ShapelessRecipeBuilder.shapelessRecipe(context.get(), 2)
                            .addIngredient(Items.RAIL)
                            .addIngredient(Items.POWERED_RAIL)
                            .addCriterion("has_rail", RegistrateRecipeProvider.hasItem(ItemTags.RAILS))
                            .build(recipeProvider)))
            .tag(ItemTags.RAILS)
            .build()
            .register();


    public static final BlockRegistryObjectGroup<DiamondCrossingRailBlock, BlockItem, ?> DIAMOND_CROSSING_RAIL =
            new BlockRegistryObjectGroup<>("diamond_crossing_rail", DiamondCrossingRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<ElevatorSwitchRailBlock, BlockItem, ?> ELEVATOR_SWITCH_RAIL =
            new BlockRegistryObjectGroup<>("elevator_switch_rail", ElevatorSwitchRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<ScaffoldingRailBlock, BlockItem, ?> SCAFFOLDING_RAIL =
            new BlockRegistryObjectGroup<>("scaffolding_rail", ScaffoldingRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<SwitchRailBlock, BlockItem, ?> SWITCH_RAIL =
            new BlockRegistryObjectGroup<>("switch_rail", SwitchRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<WyeSwitchRailBlock, BlockItem, ?> WYE_SWITCH_RAIL =
            new BlockRegistryObjectGroup<>("wye_switch_rail", WyeSwitchRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<BumperRailBlock, BlockItem, ?> BUMPER_RAIL =
            new BlockRegistryObjectGroup<>("bumper_rail", BumperRailBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    //endregion

    //region Loaders
    public static final BlockRegistryObjectGroup<LoaderBlock, BlockItem, ItemLoaderTileEntity> ITEM_LOADER =
            new BlockRegistryObjectGroup<>("item_loader", () -> new LoaderBlock(ItemLoaderTileEntity::new),
                    blockItemCreator(), ItemLoaderTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<LoaderBlock, BlockItem, FluidLoaderTileEntity> FLUID_LOADER =
            new BlockRegistryObjectGroup<>("fluid_loader", () -> new LoaderBlock(FluidLoaderTileEntity::new),
                    blockItemCreator(), FluidLoaderTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<LoaderBlock, BlockItem, EnergyLoaderTileEntity> ENERGY_LOADER =
            new BlockRegistryObjectGroup<>("energy_loader", () -> new LoaderBlock(EnergyLoaderTileEntity::new),
                    blockItemCreator(), EnergyLoaderTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);
    //endregion

    //region Assorted
    public static final BlockRegistryObjectGroup<ScaffoldingSlabBlock, BlockItem, ?> SCAFFOLDING_SLAB_BLOCK =
            new BlockRegistryObjectGroup<>("scaffolding_slab", ScaffoldingSlabBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final RegistryObject<ElevatorSwitchSupportBlock> ELEVATOR_SWITCH_SUPPORT =
            BLOCKS.register("elevator_switch_support", ElevatorSwitchSupportBlock::new);

    public static final BlockRegistryObjectGroup<ModuleConfiguratorBlock, BlockItem, ModuleConfiguratorTileEntity> MODULE_CONFIGURATOR =
            new BlockRegistryObjectGroup<>("module_configurator", ModuleConfiguratorBlock::new, blockItemCreator(),
                    ModuleConfiguratorTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<BuoyBlock, BuoyBlockItem, ?> BUOY = new BlockRegistryObjectGroup<>("buoy",
            BuoyBlock::new, buoyBlock -> new BuoyBlockItem(buoyBlock, new Item.Properties().group(Transport.ITEM_GROUP)))
            .register(BLOCKS, ITEMS);
    //endregion

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        TILE_ENTITIES.register(modBus);
        ITEMS.register(modBus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Transport.ITEM_GROUP));
    }

    private static NonNullUnaryOperator<AbstractBlock.Properties> railProperties() {
        return properties -> {
            properties.hardnessAndResistance(0.7F);
            properties.doesNotBlockMovement();
            properties.sound(SoundType.METAL);
            return properties;
        };
    }
}
