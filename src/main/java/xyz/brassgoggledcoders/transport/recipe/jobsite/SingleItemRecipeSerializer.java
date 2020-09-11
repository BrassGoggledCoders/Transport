package xyz.brassgoggledcoders.transport.recipe.jobsite;

import net.minecraft.item.crafting.SingleItemRecipe;

public class SingleItemRecipeSerializer<T extends SingleItemRecipe> extends SingleItemRecipe.Serializer<T> {
    public SingleItemRecipeSerializer(IRecipeFactory<T> factory) {
        super(factory);
    }
}
