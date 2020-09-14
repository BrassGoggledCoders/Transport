package xyz.brassgoggledcoders.transport.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class RailWorkerBenchCategory implements IRecipeCategory<RailWorkerBenchRecipe> {
    public static final ResourceLocation UID = Transport.rl("rail_workers_bench");

    private static final int inputSlot = 0;
    private static final int outputSlot = 1;

    public static final int width = 82;
    public static final int height = 34;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public RailWorkerBenchCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("jei:textures/gui/gui_vanilla.png");
        background = guiHelper.createDrawable(location, 0, 220, width, height);
        icon = guiHelper.createDrawableIngredient(new ItemStack(TransportBlocks.RAIL_WORKER_BENCH.get()));
        localizedName = I18n.format("screen.transport.jei.category.rail_workers_bench");
    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public Class<? extends RailWorkerBenchRecipe> getRecipeClass() {
        return RailWorkerBenchRecipe.class;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return localizedName;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setIngredients(RailWorkerBenchRecipe railWorkerBenchRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(railWorkerBenchRecipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, railWorkerBenchRecipe.getRecipeOutput());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setRecipe(IRecipeLayout recipeLayout, RailWorkerBenchRecipe railWorkerBenchRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(inputSlot, true, 0, 8);
        guiItemStacks.init(outputSlot, false, 60, 8);

        guiItemStacks.set(ingredients);
    }
}
