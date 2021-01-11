package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.jobsite.SingleItemSizedRecipeSerializer;

public class TransportRecipes {
    public static final IRecipeType<RailWorkerBenchRecipe> RAIL_WORKER_BENCH_TYPE = IRecipeType.register("transport:rail_workers_bench");

    public static final RegistryEntry<SingleItemSizedRecipeSerializer<RailWorkerBenchRecipe>> RAIL_WORKER_BENCH_SERIALIZER =
            Transport.getRegistrate()
                    .object("rail_workers_bench")
                    .simple(IRecipeSerializer.class, () -> new SingleItemSizedRecipeSerializer<>(RailWorkerBenchRecipe::new));

    public static void setup() {

    }
}
