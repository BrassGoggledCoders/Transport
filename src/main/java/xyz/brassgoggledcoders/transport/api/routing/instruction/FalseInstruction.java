package xyz.brassgoggledcoders.transport.api.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

import javax.annotation.Nonnull;

public class FalseInstruction extends RoutingInstruction {
    @Override
    public boolean matches(@Nonnull AbstractMinecartEntity minecartEntity) {
        return false;
    }
}
