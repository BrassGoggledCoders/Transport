package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.CommandBlockMinecartEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;

public class ComparatorRouting extends Routing {
    private final Number number;

    public ComparatorRouting(Number number) {
        this.number = number;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        int comparatorLevel = minecartEntity.getComparatorLevel();
        if (comparatorLevel < 0) {
            if (minecartEntity instanceof CommandBlockMinecartEntity) {
                comparatorLevel = ((CommandBlockMinecartEntity) minecartEntity).getCommandBlockLogic().getSuccessCount();
            } else if (minecartEntity instanceof IInventory) {
                comparatorLevel = Container.calcRedstoneFromInventory((IInventory)minecartEntity);
            }
        }
        return comparatorLevel > number.intValue();
    }
}
