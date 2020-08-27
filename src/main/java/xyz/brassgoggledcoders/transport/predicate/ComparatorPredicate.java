package xyz.brassgoggledcoders.transport.predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.CommandBlockMinecartEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParserException;

import java.util.function.Predicate;

public class ComparatorPredicate implements Predicate<Entity> {
    private final int minComparator;

    public ComparatorPredicate(int minComparator) {
        this.minComparator = minComparator;
    }

    @Override
    public boolean test(Entity entity) {
        int comparatorLevel = 0;
        if (entity instanceof AbstractMinecartEntity) {
            AbstractMinecartEntity minecartEntity = (AbstractMinecartEntity) entity;
            comparatorLevel = minecartEntity.getComparatorLevel();
            if (comparatorLevel < 0) {
                if (minecartEntity instanceof CommandBlockMinecartEntity) {
                    comparatorLevel = ((CommandBlockMinecartEntity) minecartEntity).getCommandBlockLogic().getSuccessCount();
                } else if (minecartEntity instanceof IInventory) {
                    comparatorLevel = Container.calcRedstoneFromInventory((IInventory)minecartEntity);
                }
            }
        }
        return comparatorLevel > minComparator;
    }

    public static ComparatorPredicate create(PredicateParser parser) throws PredicateParserException {
        return new ComparatorPredicate(parser.getNextInt());
    }
}
