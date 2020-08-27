package xyz.brassgoggledcoders.transport.api.predicate;

import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class StringPredicate implements Predicate<String> {
    private final BiPredicate<String, String> checker;
    private final String checkString;

    public StringPredicate(BiPredicate<String, String> checker, String checkString) {
        this.checker = checker;
        this.checkString = checkString;
    }

    @Override
    public boolean test(String test) {
        return checker.test(checkString, test);
    }

    public static ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException> create(
            BiPredicate<String, String> checker) {
        return parse -> new StringPredicate(checker, parse.getNextString());
    }
}
