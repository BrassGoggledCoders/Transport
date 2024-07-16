package xyz.brassgoggledcoders.transport.api.shell;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;

public interface IShell extends IEntityWithComplexSpawn {
    default ShellContent getContent() {
        return this.getHolder().get();
    }

    IShellContentHolder getHolder();

    default Level getShellLevel() {
        return this.getSelf().level();
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
        return Component.translatable(this.getSelf().getType().getDescriptionId() + ".with", shellContent.getName());
    }

    ItemStack asItemStack();

    @Override
    default void writeSpawnData(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.getHolder().writeToBuffer(friendlyByteBuf);
    }

    @Override
    default void readSpawnData(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.getHolder().readFromBuffer(friendlyByteBuf);
    }
}
