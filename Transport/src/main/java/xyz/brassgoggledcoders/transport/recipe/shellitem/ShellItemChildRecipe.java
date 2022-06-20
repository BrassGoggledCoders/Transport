package xyz.brassgoggledcoders.transport.recipe.shellitem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.api.recipe.ingredient.SizedIngredient;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;

import java.util.Collection;
import java.util.Collections;

public record ShellItemChildRecipe(
        ResourceLocation parentId,
        ItemStack output,
        SizedIngredient shellItem,
        SizedIngredient shellContent
) implements IRailWorkerBenchRecipe {
    @Override
    public boolean reduceContainer(Container container) {
        return false;
    }

    @Override
    public Collection<IRailWorkerBenchRecipe> getChildren() {
        return Collections.singleton(this);
    }

    @Override
    public SizedIngredient getInput() {
        return shellItem;
    }

    @Override
    public SizedIngredient getSecondaryInput() {
        return shellContent;
    }

    @Override
    public boolean matches(@NotNull Container pContainer, @NotNull Level pLevel) {
        return false;
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull Container pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @NotNull
    public ItemStack getResultItem() {
        return output();
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return parentId;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return TransportRecipes.SHELL_ITEMS.get();
    }
}
