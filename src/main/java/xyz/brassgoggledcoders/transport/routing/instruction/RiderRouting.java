package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;

public class RiderRouting extends Routing {
    private final Number number;

    public RiderRouting(Number number) {
        this.number = number;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        return minecartEntity.getPassengers().size() > number.intValue();
    }
}
