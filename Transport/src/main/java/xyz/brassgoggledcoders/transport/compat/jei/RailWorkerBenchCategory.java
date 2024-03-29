package xyz.brassgoggledcoders.transport.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;

public class RailWorkerBenchCategory implements IRecipeCategory<IRailWorkerBenchRecipe> {
    public static RecipeType<IRailWorkerBenchRecipe> JEI_RECIPE_TYPE = new RecipeType<>(
            TransportRecipes.RAIL_WORKER_BENCH.getId(),
            IRailWorkerBenchRecipe.class
    );

    private final IDrawable icon;
    private final IDrawable background;

    public RailWorkerBenchCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(new ItemStack(TransportBlocks.RAIL_WORKER_BENCH.get()));
        background = helper.createDrawable(new ResourceLocation("jei:textures/gui/gui_vanilla.png"), 0, 168, 125, 18);
    }

    @Override
    @NotNull
    public Component getTitle() {
        return TransportBlocks.RAIL_WORKER_BENCH.get().getName();
    }

    @Override
    @NotNull
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    @NotNull
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IRailWorkerBenchRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, recipe.getInput().getMatchingStacks())
                .setSlotName("input");
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, recipe.getSecondaryInput().getMatchingStacks())
                .setSlotName("secondaryInput");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1)
                .addItemStack(recipe.getResultItem())
                .setSlotName("result");
    }

    @Override
    @NotNull
    public ResourceLocation getUid() {
        return JEI_RECIPE_TYPE.getUid();
    }

    @Override
    @NotNull
    public Class<? extends IRailWorkerBenchRecipe> getRecipeClass() {
        return JEI_RECIPE_TYPE.getRecipeClass();
    }

    @Override
    @NotNull
    public RecipeType<IRailWorkerBenchRecipe> getRecipeType() {
        return JEI_RECIPE_TYPE;
    }
}