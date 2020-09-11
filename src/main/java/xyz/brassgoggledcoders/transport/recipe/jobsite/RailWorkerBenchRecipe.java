package xyz.brassgoggledcoders.transport.recipe.jobsite;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RailWorkerBenchRecipe extends SingleItemRecipe {
    public RailWorkerBenchRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
        super(TransportRecipes.RAIL_WORKER_BENCH_TYPE, TransportRecipes.RAIL_WORKER_BENCH_SERIALIZER.get(), id, group,
                ingredient, result);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(IInventory inventory, World world) {
        return this.ingredient.test(inventory.getStackInSlot(0));
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        return new ItemStack(TransportBlocks.RAIL_WORKER_BENCH.get());
    }
}
