package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class ShellMinecart extends AbstractMinecart implements IShell {
    public ShellMinecart(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    @Override
    public ShellContent getContent() {
        return null;
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        return Type.CHEST;
    }
}
