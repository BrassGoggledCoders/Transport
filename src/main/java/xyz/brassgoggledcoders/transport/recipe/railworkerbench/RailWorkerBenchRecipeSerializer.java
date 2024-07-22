package xyz.brassgoggledcoders.transport.recipe.railworkerbench;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.recipe.ingredient.SizedIngredient;
import xyz.brassgoggledcoders.transport.codec.SizedIngredientCodec;

public class RailWorkerBenchRecipeSerializer implements RecipeSerializer<RailWorkerBenchRecipe> {
    public static final Codec<RailWorkerBenchRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter(RailWorkerBenchRecipe::output),
            SizedIngredientCodec.CODEC.fieldOf("input").forGetter(RailWorkerBenchRecipe::getInput),
            SizedIngredientCodec.CODEC.optionalFieldOf("secondaryInput", SizedIngredient.EMPTY).forGetter(RailWorkerBenchRecipe::getSecondaryInput)
    ).apply(instance, RailWorkerBenchRecipe::new));

    @Override
    @NotNull
    public RailWorkerBenchRecipe fromNetwork(FriendlyByteBuf pBuffer) {
        return new RailWorkerBenchRecipe(
                pBuffer.readItem(),
                SizedIngredient.fromNetwork(pBuffer),
                SizedIngredient.fromNetwork(pBuffer)
        );
    }

    @Override
    @NotNull
    public Codec<RailWorkerBenchRecipe> codec() {
        return CODEC;
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull RailWorkerBenchRecipe pRecipe) {
        pBuffer.writeItem(pRecipe.output());
        pRecipe.input().toNetwork(pBuffer);
        pRecipe.secondaryInput().toNetwork(pBuffer);
    }
}
