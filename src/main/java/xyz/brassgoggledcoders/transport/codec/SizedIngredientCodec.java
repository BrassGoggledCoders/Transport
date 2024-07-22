package xyz.brassgoggledcoders.transport.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.item.crafting.Ingredient;
import xyz.brassgoggledcoders.transport.api.recipe.ingredient.SizedIngredient;

import java.util.Map;

public class SizedIngredientCodec implements Codec<SizedIngredient> {
    public static final Codec<SizedIngredient> CODEC = new SizedIngredientCodec();

    @Override
    public <T> DataResult<Pair<SizedIngredient, T>> decode(DynamicOps<T> ops, T input) {
        return Ingredient.CODEC_NONEMPTY.decode(ops, input)
                .flatMap(result -> ops.getMap(input)
                        .flatMap(map -> {
                            T countValue = map.get("count");
                            if (countValue != null) {
                                return ops.getNumberValue(countValue)
                                        .flatMap(number -> {
                                            int count = number.intValue();
                                            if (count > 0 && count < 65) {
                                                return DataResult.success(count);
                                            } else {
                                                return DataResult.error(() -> "count must be between 1 and 64 inclusive");
                                            }
                                        });
                            }
                            return DataResult.success(1);
                        })
                        .map(countResult -> Pair.of(new SizedIngredient(result.getFirst(), countResult), input)
                        ));
    }

    @Override
    public <T> DataResult<T> encode(SizedIngredient input, DynamicOps<T> ops, T prefix) {
        if (input.count() == 1) {
            return Ingredient.CODEC_NONEMPTY.encode(input.ingredient(), ops, prefix);
        } else {
            return Ingredient.CODEC_NONEMPTY.encode(input.ingredient(), ops, prefix)
                    .flatMap(ops::getMap)
                    .flatMap(ingredientMap -> {
                        T map = ops.createMap(Map.of(
                                ops.createString("count"),
                                ops.createInt(input.count())
                        ));
                        return ops.mergeToMap(map, ingredientMap);
                    });
        }
    }
}
