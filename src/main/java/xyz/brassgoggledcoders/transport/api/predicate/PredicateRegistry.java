package xyz.brassgoggledcoders.transport.api.predicate;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;

import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

public class PredicateRegistry {
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException>>
            ENTITY_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException>>
            STRING_PREDICATE_CREATORS = Maps.newHashMap();

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
}
