package xyz.brassgoggledcoders.transport.predicate;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParserException;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NamePredicate implements Predicate<Entity> {
    private final Predicate<String> checker;

    public NamePredicate(Predicate<String> checker) {
        this.checker = checker;
    }

    @Override
    public boolean test(Entity entity) {
        ITextComponent customName = entity.getCustomName();
        return entity.hasCustomName() && customName != null && checker.test(customName.getUnformattedComponentText());
    }

    public static NamePredicate create(PredicateParser predicateParser) throws PredicateParserException {
        if (predicateParser.hasNextString()) {
            return new NamePredicate(predicateParser.getNextString()::equals);
        } else if (predicateParser.hasNextRegex()) {
            String regex = predicateParser.getNextRegex();
            return new NamePredicate(s -> {
                try {
                    return Pattern.matches(regex, s);
                } catch (PatternSyntaxException e) {
                    return false;
                }
            });
        } else {
            return new NamePredicate(predicateParser.getNextStringPredicate());
        }
    }


}
