package xyz.brassgoggledcoders.transport.api.shellcontent;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ShellContent implements ICapabilityProvider {
    private IShell shell;
    private ShellContentCreatorInfo creatorInfo;
    private BlockState viewBlockState = Blocks.AIR.defaultBlockState();

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    public void invalidateCaps() {

    }

    @Nonnull
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public void deserializeNBT(@Nonnull CompoundTag compoundTag) {

    }

    public void setShell(@Nonnull IShell shell) {
        this.shell = shell;
    }

    @Nonnull
    public IShell getShell() {
        return Objects.requireNonNull(shell, "Called get Shell before it was set");
    }

    public void setViewBlockState(BlockState blockState) {
        this.viewBlockState = blockState;
    }

    public BlockState getViewBlockState() {
        return this.viewBlockState;
    }

    public ShellContentCreatorInfo getCreatorInfo() {
        return creatorInfo;
    }

    public void setCreatorInfo(ShellContentCreatorInfo creatorInfo) {
        this.creatorInfo = creatorInfo;
    }
}
