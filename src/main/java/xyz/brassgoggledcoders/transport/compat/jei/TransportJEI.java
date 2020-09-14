package xyz.brassgoggledcoders.transport.compat.jei;

import com.hrznstudio.titanium.util.RecipeUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

@JeiPlugin
public class TransportJEI implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = Transport.rl("jei");

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RailWorkerBenchCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        registration.addRecipes(this.getRecipes(TransportRecipes.RAIL_WORKER_BENCH_TYPE), RailWorkerBenchCategory.UID);
    }

    public <T extends IRecipe<?>> Collection<T> getRecipes(IRecipeType<T> recipeType) {
        return RecipeUtil.getRecipes(Objects.requireNonNull(Minecraft.getInstance().world), recipeType);
    }

}
