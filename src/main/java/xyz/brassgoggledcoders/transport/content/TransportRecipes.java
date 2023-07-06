package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipeSerializer;
import xyz.brassgoggledcoders.transport.recipe.shellitem.ShellItemRecipeSerializer;

public class TransportRecipes {

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(
            Registry.RECIPE_TYPE_REGISTRY,
            Transport.ID
    );

    public static final RegistryEntry<ShellItemRecipeSerializer> SHELL_ITEMS = Transport.getRegistrate()
            .object("shell_items")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, ShellItemRecipeSerializer::new);

    public static final RegistryObject<RecipeType<IRailWorkerBenchRecipe>> RAIL_WORKER_BENCH_TYPE = RECIPE_TYPE_REGISTER.register(
            "rail_worker_bench",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return Transport.ID + ":rail_worker_bench";
                }
            }
    );

    public static final RegistryEntry<RailWorkerBenchRecipeSerializer> RAIL_WORKER_BENCH = Transport.getRegistrate()
            .object("rail_worker_bench")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, RailWorkerBenchRecipeSerializer::new);

    public static void setup() {
        RECIPE_TYPE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
