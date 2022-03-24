package xyz.brassgoggledcoders.transport.api.shellcontent;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
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
        return Objects.requireNonNull(shell, "Called getShell before it was set");
    }

    public BlockState getViewBlockState() {
        return this.getCreatorInfo().blockState();
    }

    public ShellContentCreatorInfo getCreatorInfo() {
        return Objects.requireNonNull(creatorInfo, "Called getCreatorInfo before it was set");
    }

    public void setCreatorInfo(ShellContentCreatorInfo creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public void destroy(DamageSource pSource) {
        if (!pSource.isExplosion() && this.getLevel().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.getShell().getSelf().spawnAtLocation(this.getViewBlockState().getBlock());
        }
    }

    public Level getLevel() {
        return this.getShell()
                .getShellLevel();
    }
}
