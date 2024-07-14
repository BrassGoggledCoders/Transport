package xyz.brassgoggledcoders.transport.content;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipeSerializer;
import xyz.brassgoggledcoders.transport.recipe.shellitem.ShellItemRecipeSerializer;

public class TransportRecipes {

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(
            BuiltInRegistries.RECIPE_TYPE,
            Transport.ID
    );

    public static final RegistryEntry<ShellItemRecipeSerializer> SHELL_ITEMS = Transport.getRegistrate()
            .object("shell_items")
            .simple(BuiltInRegistries.RECIPE_SERIALIZER, ShellItemRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<IRailWorkerBenchRecipe>> RAIL_WORKER_BENCH_TYPE =
            RECIPE_TYPE_REGISTER.register(
                    "rail_worker_bench",
                    () -> RecipeType.simple(Transport.rl("rail_worker_bench"))
            );

    public static final RegisteringEntry<RailWorkerBenchRecipeSerializer, RecipeSerializer<?>> RAIL_WORKER_BENCH =
            Transport.getRegistering()
                    .simple("rail_worker_bench", Registries.RECIPE_SERIALIZER, RailWorkerBenchRecipeSerializer::new);

    public static void setup() {
        RECIPE_TYPE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
