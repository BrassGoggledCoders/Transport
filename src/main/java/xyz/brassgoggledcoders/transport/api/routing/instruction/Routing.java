package xyz.brassgoggledcoders.transport.api.routing.instruction;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

import javax.annotation.Nonnull;

public abstract class Routing {
    public abstract boolean matches(@Nonnull AbstractMinecartEntity minecartEntity);
}
