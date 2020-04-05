package xyz.brassgoggledcoders.transport.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;

public class PoweredRouting extends Routing {
    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        return minecartEntity.isPoweredCart();
    }
}
