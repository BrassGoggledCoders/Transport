package xyz.brassgoggledcoders.transport.shellcontent.empty;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;

public class EmptyShellContent extends ShellContent {
    private CompoundTag originalShell = new CompoundTag();

    @Override
    public void deserializeNBT(@Nonnull CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        originalShell = compoundTag;
    }

    @NotNull
    @Override
    public CompoundTag serializeNBT() {
        return originalShell.merge(super.serializeNBT());
    }
}
