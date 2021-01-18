package xyz.brassgoggledcoders.transport.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.transport.api.TransportClientAPI;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.renderer.IModuleRenderer;
import xyz.brassgoggledcoders.transport.recipe.module.ModuleRecipeBuilder;

import javax.annotation.Nonnull;

public class ModuleBuilder<M extends Module<M>, E extends M, P, B extends ModuleBuilder<M, E, P, B>> extends AbstractBuilder<M, E, P, B> {
    private final NonNullSupplier<E> moduleSupplier;
    private NonNullSupplier<NonNullSupplier<IModuleRenderer>> renderer;

    private boolean defaultRecipe;

    protected ModuleBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                            Class<? super M> registryType, NonNullSupplier<E> moduleSupplier) {
        super(owner, parent, name, callback, registryType);
        this.moduleSupplier = moduleSupplier;
        this.setData(ProviderType.RECIPE, (context, provider) -> {
            if (defaultRecipe) {
                E module = context.get();
                ModuleRecipeBuilder.create(module)
                        .withItem(Ingredient.fromItems(module.asItem()))
                        .save(provider);
            }
        });
        this.defaultRecipe = true;
    }

    public B lang(String name) {
        return this.lang(Module::getTranslationKey, name);
    }

    @SuppressWarnings("deprecation")
    public B renderer(NonNullSupplier<NonNullSupplier<IModuleRenderer>> renderer) {
        if (this.renderer == null) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> this::registerRenderer);
        }

        this.renderer = renderer;
        return this.getSelf();
    }

    public B recipe(NonNullBiConsumer<DataGenContext<M, E>, RegistrateRecipeProvider> cons) {
        defaultRecipe = false;
        return this.setData(ProviderType.RECIPE, cons);
    }

    protected void registerRenderer() {
        OneTimeEventReceiver.addModListener(FMLClientSetupEvent.class, ($) -> {
            NonNullSupplier<NonNullSupplier<IModuleRenderer>> renderer = this.renderer;
            if (renderer != null) {
                TransportClientAPI.registerModuleRenderer(this.getEntry(), this.renderer.get().get());
            }
        });
    }

    @Override
    @Nonnull
    protected @NonnullType E createEntry() {
        return this.moduleSupplier.get();
    }

    @SuppressWarnings("unchecked")
    protected B getSelf() {
        return (B) this;
    }
}
