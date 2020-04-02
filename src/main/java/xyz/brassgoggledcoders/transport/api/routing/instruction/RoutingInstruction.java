package xyz.brassgoggledcoders.transport.api.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

import javax.annotation.Nonnull;

public abstract class RoutingInstruction {
    public abstract boolean matches(@Nonnull AbstractMinecartEntity minecartEntity);
}
