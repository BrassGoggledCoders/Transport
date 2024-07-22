package xyz.brassgoggledcoders.transport.recipe.shellitem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ShellItemRecipeSerializer implements RecipeSerializer<ShellItemRecipe> {
    public static final Codec<ShellItemRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(ShellItemRecipe::getInternalInput),
            ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter(ShellItemRecipe::getOutput)
    ).apply(instance, ShellItemRecipe::new));


    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public ShellItemRecipe fromNetwork(FriendlyByteBuf pBuffer) {
        return new ShellItemRecipe(
                Ingredient.fromNetwork(pBuffer),
                pBuffer.readItem()
        );
    }

    @Override
    @NotNull
    public Codec<ShellItemRecipe> codec() {
        return CODEC;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(FriendlyByteBuf pBuffer, ShellItemRecipe pRecipe) {
        pRecipe.getInput().ingredient().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.getOutput());
    }
}
