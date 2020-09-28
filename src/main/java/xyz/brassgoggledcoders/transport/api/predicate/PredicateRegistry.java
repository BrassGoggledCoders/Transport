package xyz.brassgoggledcoders.transport.api.predicate;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateRegistry {
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException>>
            ENTITY_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException>>
            STRING_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<Item, Function<ItemStack, PredicateParser>> ITEM_TO_PREDICATE_PARSERS = Maps.newHashMap();

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
    public static PredicateParser getPredicateParserFromItemStack(ItemStack itemStack) {
        Function<ItemStack, PredicateParser> parserFunction = ITEM_TO_PREDICATE_PARSERS.get(itemStack.getItem());
        if (parserFunction != null) {
            return parserFunction.apply(itemStack);
        } else {
            return null;
        }
    }

    public static void addItemToPredicateParser(Item item, Function<ItemStack, PredicateParser> parserFunction) {
        ITEM_TO_PREDICATE_PARSERS.put(item, parserFunction);
    }
}
