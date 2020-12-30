package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.container.locomotive.SteamLocomotiveContainer;
import xyz.brassgoggledcoders.transport.container.provider.EntityContainerProvider;
import xyz.brassgoggledcoders.transport.engine.SteamEngine;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SteamLocomotiveEntity extends LocomotiveEntity {
    private final SteamEngine steamEngine;

    public SteamLocomotiveEntity(EntityType<? extends SteamLocomotiveEntity> type, World world) {
        super(type, world);
        this.steamEngine = new SteamEngine(this::isOn);
    }

    @Override
    public void tick() {
        super.tick();
        this.steamEngine.tick();
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (!player.isCrouching()) {
            if (player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, new EntityContainerProvider<>(this,
                        SteamLocomotiveContainer::new));
            }
            return ActionResultType.func_233537_a_(this.getEntityWorld().isRemote());
        } else {
            return super.processInitialInteract(player, hand);
        }
    }

    public SteamEngine getSteamEngine() {
        return this.steamEngine;
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("steamEngine", steamEngine.serializeNBT());
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        steamEngine.deserializeNBT(compound.getCompound("steamEngine"));
    }
}
