package xyz.brassgoggledcoders.transport.recipe.jobsite;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.SizedIngredient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RailWorkerBenchRecipe extends SingleItemSizedRecipe {
    public RailWorkerBenchRecipe(ResourceLocation id, String group, SizedIngredient ingredient, ItemStack result) {
        super(TransportRecipes.RAIL_WORKER_BENCH_TYPE, TransportRecipes.RAIL_WORKER_BENCH_SERIALIZER.get(), id, group,
                ingredient, result);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(IInventory inventory, World world) {
        return this.getIngredient().test(inventory.getStackInSlot(0));
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        return new ItemStack(TransportBlocks.RAIL_WORKER_BENCH.get());
    }
}
