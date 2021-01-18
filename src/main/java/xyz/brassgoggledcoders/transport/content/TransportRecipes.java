package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.jobsite.SingleItemSizedRecipeSerializer;
import xyz.brassgoggledcoders.transport.recipe.module.ModuleRecipe;
import xyz.brassgoggledcoders.transport.recipe.module.ModuleRecipeSerializer;

public class TransportRecipes {
    public static final IRecipeType<RailWorkerBenchRecipe> RAIL_WORKER_BENCH_TYPE = IRecipeType.register("transport:rail_workers_bench");

    public static final RegistryEntry<SingleItemSizedRecipeSerializer<RailWorkerBenchRecipe>> RAIL_WORKER_BENCH_SERIALIZER =
            Transport.getRegistrate()
                    .object("rail_workers_bench")
                    .simple(IRecipeSerializer.class, () -> new SingleItemSizedRecipeSerializer<>(RailWorkerBenchRecipe::new));

    public static final IRecipeType<ModuleRecipe> MODULE_RECIPE_TYPE = IRecipeType.register("transport:module_recipe");

    public static final RegistryEntry<ModuleRecipeSerializer> MODULE_RECIPE_SERIALIZER =
            Transport.getRegistrate()
                    .object("module")
                    .simple(IRecipeSerializer.class, ModuleRecipeSerializer::new);

    public static void setup() {

    }
}
