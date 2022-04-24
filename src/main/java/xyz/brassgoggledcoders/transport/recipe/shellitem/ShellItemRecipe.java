package xyz.brassgoggledcoders.transport.recipe.shellitem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.ingredient.SizedIngredient;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Optional;

public record ShellItemRecipe(
        ResourceLocation id,
        Ingredient input,
        ItemStack output
) implements IRailWorkerBenchRecipe {
    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(Container pContainer, Level pLevel) {
        return findMatching(pContainer)
                .map(tuple -> {
                    for (ShellContentCreatorInfo info : TransportAPI.SHELL_CONTENT_CREATOR.get().getAll()) {
                        if (info.createRecipe() && info.viewState().getBlock().asItem() == tuple.getRight().getItem()) {
                            return true;
                        }
                    }
                    return false;
                }).orElse(false);
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull Container pContainer) {
        return findMatching(pContainer)
                .map(tuple -> {
                    for (ShellContentCreatorInfo info : TransportAPI.SHELL_CONTENT_CREATOR.get().getAll()) {
                        if (info.createRecipe() && info.viewState().getBlock().asItem() == tuple.getRight().getItem()) {
                            ItemStack itemStack = this.output().copy();
                            CompoundTag shellContent = new CompoundTag();
                            shellContent.putString("id", info.id().toString());
                            itemStack.addTagElement("shellContent", shellContent);
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
        return output().copy();
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return this.id();
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
                        id(),
                        info.embedNBT(output().copy()),
                        this.getInput(),
                        SizedIngredient.of(Ingredient.of(info.viewState().getBlock()))
                ))
                .toList();
    }

    @Override
    public SizedIngredient getInput() {
        return SizedIngredient.of(this.input);
    }

    @Override
    public SizedIngredient getSecondaryInput() {
        return SizedIngredient.of(Ingredient.EMPTY);
    }
}
