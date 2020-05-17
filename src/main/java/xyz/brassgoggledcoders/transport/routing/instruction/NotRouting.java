package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;

public class NotRouting extends Routing {
    private final Routing routing;

    public NotRouting(Routing routing) {
        this.routing = routing;
    }

    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        return !routing.matches(minecartEntity);
    }
}
