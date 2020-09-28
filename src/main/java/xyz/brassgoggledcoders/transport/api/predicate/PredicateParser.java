package xyz.brassgoggledcoders.transport.api.predicate;

import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class PredicateParser {
    private static final Predicate<String> STRING_PATTERN = Pattern.compile("^\\s*\".+\".*").asPredicate();
    private static final Predicate<String> PREDICATE_START_PATTERN = Pattern.compile("^\\s*\\w+\\s*\\{.*").asPredicate();
    private static final Predicate<String> NUMBER_PATTERN = Pattern.compile("^\\s*\\d*.*").asPredicate();

    private final Function<String, ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException>>
            getEntityPredicateCreator;
    private final Function<String, ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException>>
            getStringPredicateCreator;

    private final String parsing;
    private int parsedLocation;

    private PredicateParser(String parsing) {
        this(parsing, PredicateRegistry::getEntityPredicateCreator, PredicateRegistry::getStringPredicateCreator);
    }

    private PredicateParser(String parsing, Function<String, ThrowingFunction<PredicateParser, Predicate<Entity>,
            PredicateParserException>> getEntityPredicateCreator, Function<String, ThrowingFunction<PredicateParser,
            Predicate<String>, PredicateParserException>> getStringPredicateCreator) {
        this.parsing = parsing;
        this.parsedLocation = 0;
        this.getStringPredicateCreator = getStringPredicateCreator;
        this.getEntityPredicateCreator = getEntityPredicateCreator;
    }


    public String getNextString() throws PredicateParserException {
        int startQuoteIndex = parsing.indexOf('\"', parsedLocation);
        if (startQuoteIndex < 0) {
            throw new PredicateParserException("Failed to Find Start of String");
        }
        int endQuoteIndex = parsing.indexOf('\"', startQuoteIndex + 1);
        if (endQuoteIndex < 0) {
            throw new PredicateParserException("Failed to Find End of String");
        }

        parsedLocation = endQuoteIndex + 1;
        return parsing.substring(startQuoteIndex + 1, endQuoteIndex);
    }

    public boolean hasNextString() {
        return STRING_PATTERN.test(parsing.substring(parsedLocation));
    }

    public int getNextInt() throws PredicateParserException {
        boolean foundNumber = false;
        int startIndex = parsedLocation;
        int endIndex = -1;
        while (endIndex < 0) {
            char currentChar = parsing.charAt(parsedLocation);
            if (Character.isWhitespace(currentChar)) {
                if (foundNumber) {
                    endIndex = parsedLocation;
                }
            } else if (Character.isDigit(currentChar)) {
                foundNumber = true;
            }
            nextParseLocation();
        }
        String numberToParse = parsing.substring(startIndex, endIndex).trim();
        try {
            return Integer.parseInt(numberToParse);
        } catch (NumberFormatException exception) {
            throw new PredicateParserException(numberToParse + " is not a valid number");
        }
    }

    public boolean hasNextInt() {
        return NUMBER_PATTERN.test(parsing.substring(parsedLocation));
    }

    public Predicate<Entity> getNextEntityPredicate() throws PredicateParserException {
        return this.getNextPredicate(getEntityPredicateCreator);
    }

    public Predicate<String> getNextStringPredicate() throws PredicateParserException {
        return this.getNextPredicate(getStringPredicateCreator);
    }

    public boolean hasNextPredicate() {
        return PREDICATE_START_PATTERN.test(parsing.substring(parsedLocation));
    }

    public <T> Predicate<T> getNextPredicate(Function<String, ThrowingFunction<PredicateParser, Predicate<T>,
            PredicateParserException>> getPredicateCreator) throws PredicateParserException {
        int startBracketIndex = parsing.indexOf('{', parsedLocation);
        if (startBracketIndex < 0) {
            throw new PredicateParserException("Failed to Find Start { of Predicate");
        }
        String predicateName = parsing.substring(parsedLocation, startBracketIndex).trim();
        ThrowingFunction<PredicateParser, Predicate<T>, PredicateParserException> predicateCreator = getPredicateCreator.apply(predicateName);
        if (predicateCreator == null) {
            throw new PredicateParserException("Failed to Find Predicate for Name: " + predicateName);
        }

        int numberOfOpenPredicates = 1;
        parsedLocation = startBracketIndex;
        while (numberOfOpenPredicates > 0) {
            nextParseLocation();
            char charAt = parsing.charAt(parsedLocation);
            if (charAt == '}') {
                numberOfOpenPredicates--;
            } else if (charAt == '{') {
                numberOfOpenPredicates++;
            }

        }

        return predicateCreator.apply(PredicateParser.fromString(parsing.substring(startBracketIndex + 1, parsedLocation++)));
    }

    private void nextParseLocation() throws PredicateParserException {
        parsedLocation++;
        if (parsedLocation > parsing.length()) {
            throw new PredicateParserException("Failed to Find Closing } of Predicate");
        }
    }

    public static PredicateParser fromString(String parsing) {
        return new PredicateParser(parsing);
    }
}
