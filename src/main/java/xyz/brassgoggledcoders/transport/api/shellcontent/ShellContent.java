package xyz.brassgoggledcoders.transport.api.shellcontent;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shell.IShell;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ShellContent implements ICapabilityProvider {
    private IShell shell;
    private ShellContentCreatorInfo creatorInfo;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        return InteractionResult.PASS;
    }

    public void invalidateCaps() {

    }

    public void reviveCaps() {

    }

    @Nonnull
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    public void deserializeNBT(@Nonnull CompoundTag compoundTag) {

    }

    @Nonnull
    public IShell getShell() {
        return Objects.requireNonNull(shell, "Called getShell before it was set");
    }

    public void setShell(@Nonnull IShell shell) {
        this.shell = shell;
    }

    public BlockState getViewBlockState() {
        return this.getCreatorInfo().viewState();
    }

    public Component getName() {
        return this.getCreatorInfo().name();
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

    public boolean stillValid(Player pPlayer) {
        if (this.getShell().getSelf().isRemoved()) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr(this.getShell().getSelf()) > 64.0D);
        }
    }
}
