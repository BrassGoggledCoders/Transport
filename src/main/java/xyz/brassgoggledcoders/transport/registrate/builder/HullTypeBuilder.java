package xyz.brassgoggledcoders.transport.registrate.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.recipe.nbtshapedrecipebuilder.NBTShapedRecipeBuilder;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class HullTypeBuilder<T extends HullType, I extends Item, P> extends AbstractBuilder<HullType, T, P, HullTypeBuilder<T, I, P>> {
    private final Function<NonNullSupplier<I>, T> hullTypeCreator;
    private NonNullSupplier<I> hullItemSupplier;

    public static <T extends HullType, I extends Item, P> HullTypeBuilder<T, I, P> create(AbstractRegistrate<?> owner, P parent,
                                                                                          String name, BuilderCallback builderCallback,
                                                                                          Function<NonNullSupplier<I>, T> hullCreator) {
        return new HullTypeBuilder<>(owner, parent, name, builderCallback, hullCreator);
    }

    private HullTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                            Function<NonNullSupplier<I>, T> hullTypeCreator) {
        super(owner, parent, name, callback, HullType.class);
        this.hullTypeCreator = hullTypeCreator;
    }

    public HullTypeBuilder<T, I, P> lang(String name) {
        return this.lang(HullType::getTranslationKey, name);
    }

    public HullTypeBuilder<T, I, P> item(NonNullSupplier<I> item) {
        this.hullItemSupplier = item;
        return this;
    }

    public HullTypeBuilder<T, I, P> item(NonNullBiFunction<Supplier<? extends T>, Item.Properties, ? extends I> factory,
                                         NonNullFunction<ItemBuilder<I, HullTypeBuilder<T, I, P>>,
                                                 ItemBuilder<I, HullTypeBuilder<T, I, P>>> itemBuilder) {
        this.hullItemSupplier = itemBuilder.apply(getOwner().item(this, getName(),
                p -> factory.apply(this::getEntry, p))).register();
        return this;
    }

    public HullTypeBuilder<T, I, P> item(String name,
                                         NonNullBiFunction<Supplier<? extends T>, Item.Properties, ? extends I> factory,
                                         NonNullFunction<ItemBuilder<I, HullTypeBuilder<T, I, P>>,
                                                 ItemBuilder<I, HullTypeBuilder<T, I, P>>> itemBuilder) {
        this.hullItemSupplier = itemBuilder.apply(getOwner().item(this, getName() + "_" + name,
                p -> factory.apply(this::getEntry, p))).register();
        return this;
    }

    public HullTypeBuilder<T, I, P> itemTag(ITag.INamedTag<Item> tag) {
        return this.setData(ProviderType.ITEM_TAGS, (context, provider) -> provider.getOrCreateBuilder(tag)
                .add(context.get().asItem()));
    }

    public HullTypeBuilder<T, I, P> defaultRecipe(NonNullSupplier<Item> output) {
        return recipe((context, provider) -> {
            CompoundNBT tagNBT = new CompoundNBT();
            tagNBT.putString("hull_type", context.getId().toString());
            NBTShapedRecipeBuilder.shapedRecipe(output.get(), tagNBT)
                    .patternLine(" S ")
                    .patternLine("RHR")
                    .patternLine(" S ")
                    .key('S', Tags.Items.SLIMEBALLS)
                    .key('R', Tags.Items.DUSTS_REDSTONE)
                    .key('H', Ingredient.fromItems(context.get().asItem()))
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(context.get().asItem()))
                    .build(provider, ForgeRegistries.ITEMS.getKey(output.get()) + "_from_" + this.getName());
        });
    }

    public HullTypeBuilder<T, I, P> recipe(NonNullBiConsumer<DataGenContext<HullType, T>, RegistrateRecipeProvider> cons) {
        return this.setData(ProviderType.RECIPE, cons);
    }

    @Override
    @Nonnull
    protected T createEntry() {
        Objects.requireNonNull(hullItemSupplier, "Item is required for HullTypes");
        return hullTypeCreator.apply(hullItemSupplier);
    }
}
