package xyz.brassgoggledcoders.transport.api.predicate;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.transport.api.bookholder.IBookHolder;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class PredicateRegistry {
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException>>
            ENTITY_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException>>
            STRING_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<Item, BiFunction<ItemStack, IBookHolder, String>> ITEM_TO_PREDICATE_INPUT = Maps.newHashMap();

    public static void addEntityPredicateCreator(String name, ThrowingFunction<PredicateParser, Predicate<Entity>,
            PredicateParserException> entityPredicateCreator) {
        ENTITY_PREDICATE_CREATORS.put(name.toUpperCase(Locale.US), entityPredicateCreator);
    }

    public static ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException> getEntityPredicateCreator(String name) {
        return ENTITY_PREDICATE_CREATORS.get(name.toUpperCase(Locale.US));
    }

    public static void addStringPredicateCreator(String name, ThrowingFunction<PredicateParser, Predicate<String>,
            PredicateParserException> stringPredicateCreator) {
        STRING_PREDICATE_CREATORS.put(name.toUpperCase(Locale.US), stringPredicateCreator);
    }

    public static ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException> getStringPredicateCreator(String name) {
        return STRING_PREDICATE_CREATORS.get(name.toUpperCase(Locale.US));
    }

    @Nullable
    public static PredicateParser getPredicateParserFromItemStack(ItemStack itemStack, @Nullable IBookHolder bookHolder) {
        BiFunction<ItemStack, IBookHolder, String> parserFunction = ITEM_TO_PREDICATE_INPUT.get(itemStack.getItem());
        if (parserFunction != null) {
            String predicateInput = parserFunction.apply(itemStack, bookHolder);
            if (predicateInput != null) {
                return PredicateParser.fromString(predicateInput);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public static String getPredicateInputFromItemStack(ItemStack itemStack, @Nullable IBookHolder bookHolder) {
        BiFunction<ItemStack, IBookHolder, String> parserFunction = ITEM_TO_PREDICATE_INPUT.get(itemStack.getItem());
        if (parserFunction != null) {
            return parserFunction.apply(itemStack, bookHolder);
        } else {
            return null;
        }
    }

    public static void addItemToPredicateInput(Item item, BiFunction<ItemStack, IBookHolder, String> parserFunction) {
        ITEM_TO_PREDICATE_INPUT.put(item, parserFunction);
    }
}
