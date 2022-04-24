package xyz.brassgoggledcoders.transport.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportItems;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;

import javax.annotation.Nonnull;
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
        registration.addRecipes(
                RailWorkerBenchCategory.JEI_RECIPE_TYPE,
                Objects.requireNonNull(Minecraft.getInstance().level)
                        .getRecipeManager()
                        .getAllRecipesFor(TransportRecipes.RAIL_WORKER_BENCH_TYPE.get())
                        .stream()
                        .flatMap(recipe -> recipe.getChildren().stream())
                        .toList()
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(TransportBlocks.RAIL_WORKER_BENCH.get()),
                RailWorkerBenchCategory.JEI_RECIPE_TYPE
        );
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(TransportItems.SHELL_MINECART.get());
    }
}
