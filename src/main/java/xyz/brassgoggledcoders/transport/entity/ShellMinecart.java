package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.ClientShellContentHolder;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.ServerShellContentHolder;
import xyz.brassgoggledcoders.transport.content.TransportItems;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ShellMinecart extends AbstractMinecart implements IShell {
    private final IShellContentHolder holder;

    public ShellMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.holder = IShellContentHolder.createForSide(this);
    }

    public ShellMinecart(EntityType<?> entityType, Level level, Vec3 vec3, ShellContent shellContent) {
        super(entityType, level, vec3.x(), vec3.y(), vec3.z());
        this.holder = IShellContentHolder.createForSide(this);
        this.holder.update(shellContent);
    }

    @Override
    public IShellContentHolder getHolder() {
        return this.holder;
    }

    @Override
    public Entity getSelf() {
        return this;
    }

    @Override
    public ItemStack asItemStack() {
        return new ItemStack(TransportItems.SHELL_MINECART.get());
    }

    @Override
    public ItemStack getPickResult() {
        return this.holder.asItemStack();
    }

    @Override
    public void destroy(@Nonnull DamageSource pSource) {
        super.destroy(pSource);
        this.getContent().destroy(pSource);
    }

    @Override
    @NotNull
    protected Item getDropItem() {
        return TransportItems.SHELL_MINECART.get();
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("shellContent")) {
            CompoundTag shellContents = pCompound.getCompound("shellContent");
            this.getHolder().update(TransportAPI.SHELL_CONTENT_CREATOR.get()
                    .create(
                            new ResourceLocation(shellContents.getString("id")),
                            shellContents.getCompound("data")
                    )
            );
        } else {
            this.getHolder().deserializeNBT(pCompound.getCompound(ShellContentCreatorInfo.NBT_TAG_ELEMENT));
        }

    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put(ShellContentCreatorInfo.NBT_TAG_ELEMENT, this.getHolder().serializeNBT());
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
