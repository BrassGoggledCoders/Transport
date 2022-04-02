package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.ClientShellContentHolder;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.ServerShellContentHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ShellMinecart extends AbstractMinecart implements IShell, IEntityAdditionalSpawnData {
    private final IShellContentHolder holder;

    public ShellMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
        if (level.isClientSide()) {
            this.holder = new ClientShellContentHolder(this);
        } else {
            this.holder = new ServerShellContentHolder(TransportAPI.SHELL_CONTENT_CREATOR.get(), this);
        }
    }

    public ShellMinecart(EntityType<?> entityType, Level level, Vec3 vec3, ShellContent shellContent) {
        super(entityType, level, vec3.x(), vec3.y(), vec3.z());
        if (level.isClientSide()) {
            this.holder = new ClientShellContentHolder(this);
        } else {
            this.holder = new ServerShellContentHolder(TransportAPI.SHELL_CONTENT_CREATOR.get(), this);
        }
        this.holder.update(shellContent);
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
        TransportAPI.SHELL_NETWORKING.get()
                .newGeneration(this);
    }

    @Override
    public Entity getSelf() {
        return this;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        this.getHolder().writeToBuffer(buffer);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.getHolder().readFromBuffer(additionalData);
    }

    @Override
    public void destroy(@Nonnull DamageSource pSource) {
        super.destroy(pSource);
        this.getContent().destroy(pSource);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        CompoundTag shellContents = pCompound.getCompound("shellContent");
        this.getHolder().update(TransportAPI.SHELL_CONTENT_CREATOR.get()
                .create(
                        new ResourceLocation(shellContents.getString("id")),
                        shellContents.getCompound("data")
                )
        );
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        ShellContent shellContent = this.getContent();
        CompoundTag shellContentNbt = new CompoundTag();
        shellContentNbt.putString("id", shellContent.getCreatorInfo().id().toString());
        shellContentNbt.put("data", shellContent.serializeNBT());
        pCompound.put("shellContent", shellContentNbt);
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        return Type.CHEST;
    }

    @Override
    @Nonnull
    public BlockState getDefaultDisplayBlockState() {
        return this.getContent().getViewBlockState();
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.getContent().invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.getContent().reviveCaps();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> shellCap = this.getContent().getCapability(cap, side);
        if (shellCap.isPresent()) {
            return shellCap;
        }

        return super.getCapability(cap, side);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult result = super.interact(player, hand);
        if (result.consumesAction()) {
            return result;
        }

        result = this.getContent().interact(player, hand);
        if (result.consumesAction()) {
            return result;
        }

        return InteractionResult.PASS;
    }

    @Override
    @Nonnull
    public Component getName() {
        if (this.hasCustomName()) {
            return super.getName();
        } else {
            return this.getHolder().getName();
        }
    }
}
