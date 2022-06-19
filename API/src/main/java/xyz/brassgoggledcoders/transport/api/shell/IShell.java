package xyz.brassgoggledcoders.transport.api.shell;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;

public interface IShell {
    default ShellContent getContent() {
        return this.getHolder().get();
    }

    IShellContentHolder getHolder();

    default Level getShellLevel() {
        return this.getSelf().getLevel();
    }

    default int getShellId() {
        return this.getSelf().getId();
    }

    default void newGeneration() {
        TransportAPI.SHELL_NETWORKING.get()
                .newGeneration(this);
    }

    Entity getSelf();

    default Component getWithName(ShellContent shellContent) {
        return new TranslatableComponent(this.getSelf().getType().getDescriptionId() + ".with", shellContent.getName());
    }
}
