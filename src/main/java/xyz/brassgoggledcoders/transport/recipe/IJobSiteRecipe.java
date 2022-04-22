package xyz.brassgoggledcoders.transport.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import xyz.brassgoggledcoders.transport.recipe.ingredient.SizedIngredient;

public interface IJobSiteRecipe extends Recipe<Container> {
    boolean reduceContainer(Container container);
}
