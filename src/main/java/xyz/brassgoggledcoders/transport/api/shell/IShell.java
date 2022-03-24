package xyz.brassgoggledcoders.transport.api.shell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;

public interface IShell {
    ShellContent getContent();

    IShellContentHolder getHolder();

    Level getShellLevel();

    int getShellId();

    void newGeneration();

    Entity getSelf();
}
