package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;

public class ComparatorRouting extends Routing {
    private final Number number;

    public ComparatorRouting(Number number) {
        this.number = number;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        return minecartEntity.getComparatorLevel() > number.intValue();
    }
}
