package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.ServerShellContentHolder;

import javax.annotation.Nonnull;

public class ShellMinecart extends AbstractMinecart implements IShell {
    private IShellContentHolder holder;

    public ShellMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public ShellMinecart(EntityType<?> entityType, Level level, ServerShellContentHolder holder) {
        super(entityType, level);
        this.holder = holder;
    }

    @Override
    public ShellContent getContent() {
        return this.holder.get();
    }

    @Override
    public IShellContentHolder getHolder() {
        return this.holder;
    }

    @Override
    public Level getShellLevel() {
        return this.getLevel();
    }

    @Override
    public int getShellId() {
        return this.getId();
    }

    @Override
    public void newGeneration() {

    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        return Type.CHEST;
    }
}
