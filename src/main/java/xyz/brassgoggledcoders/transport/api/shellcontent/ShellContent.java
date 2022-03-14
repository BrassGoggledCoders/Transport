package xyz.brassgoggledcoders.transport.api.shellcontent;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

import java.util.Objects;

public class ShellContent implements ICapabilityProvider {
    private IShell shell;

    public void setShell(IShell shell) {
        this.shell = shell;
    }

    public IShell getShell() {
        return Objects.requireNonNull(shell, "Called get Shell before it was set");
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    public void invalidateCaps() {

    }

    public BlockState getView() {
        return Blocks.AIR.defaultBlockState();
    }
}
