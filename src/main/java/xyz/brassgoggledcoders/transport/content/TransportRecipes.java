package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.jobsite.SingleItemRecipeSerializer;

public class TransportRecipes {
    public static final IRecipeType<RailWorkerBenchRecipe> RAIL_WORKER_BENCH_TYPE = IRecipeType.register("transport:rail_worker");

    public static final RegistryEntry<SingleItemRecipeSerializer<RailWorkerBenchRecipe>> RAIL_WORKER_BENCH_SERIALIZER =
            Transport.getRegistrate()
                    .object("rail_worker")
                    .simple(IRecipeSerializer.class, () -> new SingleItemRecipeSerializer<>(RailWorkerBenchRecipe::new));

    public static void setup() {

    }
}
