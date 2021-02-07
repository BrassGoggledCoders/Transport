package xyz.brassgoggledcoders.transport.predicate;

import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParserException;

import java.util.function.Predicate;

public class RiderPredicate implements Predicate<Entity> {
    private final int atLeastRiders;

    public RiderPredicate(int atLeastRiders) {
        this.atLeastRiders = atLeastRiders;
    }
    
    @Override
    public boolean test(Entity entity) {
        return entity.getPassengers().size() >= atLeastRiders;
    }

    public static RiderPredicate create(PredicateParser predicateParser) throws PredicateParserException {
        return new RiderPredicate(predicateParser.getNextInt());
    }
}
