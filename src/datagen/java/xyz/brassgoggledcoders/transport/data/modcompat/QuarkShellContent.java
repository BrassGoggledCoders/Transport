package xyz.brassgoggledcoders.transport.data.modcompat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.data.shellcontent.ShellContentInfoBuilder;
import xyz.brassgoggledcoders.transport.data.shellcontent.builders.ItemStorageShellContentBuilder;
import xyz.brassgoggledcoders.transport.shellcontent.storage.item.StorageSize;

import java.util.Collection;
import java.util.function.BiConsumer;

public class QuarkShellContent {
    private static final String[] CHEST_NAMES = new String[]{
            "oak_chest",
            "spruce_chest",
            "jungle_chest",
            "acacia_chest",
            "birch_chest",
            "dark_oak_chest",
            "crimson_chest",
            "warped_chest",
            "nether_brick_chest",
            "purpur_chest",
            "prismarine_chest",
            "azalea_chest",
            "blossom_chest"
    };

    private static final ICondition QUARK_LOADED = new ModLoadedCondition("quark");

    public static void gather(BiConsumer<Collection<ICondition>, ShellContentCreatorInfo> consumer) {
        if (ModList.get().isLoaded("quark")) {
            for (String chestName : CHEST_NAMES) {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("quark", chestName));
                if (block == null || block == Blocks.AIR) {
                    throw new IllegalStateException("Failed to find Quark Chest " + chestName);
                }

                ShellContentInfoBuilder.of(block)
                        .withConditions(QUARK_LOADED)
                        .withShellContentCreator(ItemStorageShellContentBuilder.of(StorageSize.THREE_BY_NINE))
                        .build(consumer);
            }
        }

    }
}
