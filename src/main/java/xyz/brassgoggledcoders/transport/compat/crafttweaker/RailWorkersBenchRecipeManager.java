package xyz.brassgoggledcoders.transport.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;

@ZenRegister
@ZenCodeType.Name("mods.transport.RailWorkersBenchRecipeManager")
public class RailWorkersBenchRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient input, IItemStack result) {
        final ItemStack resultItemStack = result.getInternal();
        final Ingredient ingredient = input.asVanillaIngredient();
        final RailWorkerBenchRecipe recipe = new RailWorkerBenchRecipe(Transport.rl(name), "", ingredient, resultItemStack);

        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, null));
    }

    @Override
    public IRecipeType<?> getRecipeType() {
        return TransportRecipes.RAIL_WORKER_BENCH_TYPE;
    }
}
