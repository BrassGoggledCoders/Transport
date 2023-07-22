package xyz.brassgoggledcoders.transport.recipe.shellitem;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.recipe.ingredient.SizedIngredient;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Optional;

public class ShellItemRecipe implements IRailWorkerBenchRecipe {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final Lazy<Ingredient> ingredient;

    public ShellItemRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.ingredient = Lazy.of(() -> Ingredient.of(TransportAPI.SHELL_CONTENT_CREATOR.get()
                .getAll()
                .stream()
                .filter(ShellContentCreatorInfo::createRecipe)
                .<ItemLike>map(info -> info.viewState()
                        .getBlock()
                        .asItem()
                )
                .toArray(ItemLike[]::new)
        ));
    }

    public ShellItemRecipe(ResourceLocation id, Ingredient input, ItemStack output, Ingredient secondary) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.ingredient = () -> secondary;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(Container pContainer, Level pLevel) {
        return findMatching(pContainer)
                .filter(pair -> this.ingredient.get().test(pair.getRight()))
                .isPresent();
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull Container pContainer) {
        return findMatching(pContainer)
                .map(tuple -> {
                    for (ShellContentCreatorInfo info : TransportAPI.SHELL_CONTENT_CREATOR.get().getAll()) {
                        if (info.createRecipe() && info.viewState().getBlock().asItem() == tuple.getRight().getItem()) {
                            ItemStack itemStack = this.getOutput().copy();
                            info.embedNBT(itemStack);
                            return itemStack;
                        }
                    }
                    return ItemStack.EMPTY;
                }).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pHeight * pWidth >= 2;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return getOutput().copy();
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return TransportRecipes.SHELL_ITEMS.get();
    }

    private Optional<Pair<ItemStack, ItemStack>> findMatching(Container pContainer) {
        ItemStack matchesInput = null;
        ItemStack matchesOther = null;
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack currentStack = pContainer.getItem(i);
            if (!currentStack.isEmpty()) {
                if (input.test(currentStack)) {
                    if (matchesInput == null) {
                        matchesInput = currentStack;
                    } else {
                        return Optional.empty();
                    }
                } else {
                    if (matchesOther == null) {
                        matchesOther = currentStack;
                    } else {
                        return Optional.empty();
                    }
                }
            }
        }
        return matchesInput != null && matchesOther != null ? Optional.of(Pair.of(matchesInput, matchesOther)) : Optional.empty();
    }

    @Override
    public boolean reduceContainer(Container container) {
        return !container.removeItem(0, 1).isEmpty() && !container.removeItem(1, 1).isEmpty();
    }

    @Override
    public Collection<IRailWorkerBenchRecipe> getChildren() {
        return TransportAPI.SHELL_CONTENT_CREATOR.get()
                .getAll()
                .stream()
                .filter(ShellContentCreatorInfo::createRecipe)
                .<IRailWorkerBenchRecipe>map(info -> new ShellItemChildRecipe(
                        this.getId(),
                        info.embedNBT(getOutput().copy()),
                        this.getInput(),
                        SizedIngredient.of(Ingredient.of(info.viewState().getBlock()))
                ))
                .toList();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public SizedIngredient getInput() {
        return SizedIngredient.of(this.input);
    }

    @Override
    public SizedIngredient getSecondaryInput() {
        return SizedIngredient.of(Ingredient.EMPTY);
    }

    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.getInput().ingredient());
        ingredients.add(this.ingredient.get());
        return ingredients;
    }

    public ItemStack getOutput() {
        return output;
    }

    public Ingredient getSecondary() {
        return this.ingredient.get();
    }
}
