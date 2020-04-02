package xyz.brassgoggledcoders.transport.api.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

import javax.annotation.Nonnull;

public class TrueInstruction extends RoutingInstruction {
    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        return true;
    }
}
