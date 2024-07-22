package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.api.shellcontent.holder.IShellContentHolder;
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
        this.kill();
        this.getContent().destroy(pSource);
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = this.getHolder()
                    .asItemStack();
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }

            this.spawnAtLocation(itemstack);
        }
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
            this.getHolder().load(pCompound.getCompound(ShellContentCreatorInfo.NBT_TAG_ELEMENT));
        }

    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        CompoundTag holderTag = new CompoundTag();
        this.getHolder().save(holderTag);
        pCompound.put(ShellContentCreatorInfo.NBT_TAG_ELEMENT, holderTag);
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
