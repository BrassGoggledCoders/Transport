package xyz.brassgoggledcoders.transport.shellcontent.empty;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public class EmptyShellContent extends ShellContent {
    private CompoundTag originalShell;

    @Override
    public void deserializeNBT(@NotNull CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        originalShell = compoundTag;
    }

    @NotNull
    @Override
    public CompoundTag serializeNBT() {
        return originalShell.merge(super.serializeNBT());
    }
}
