package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.container.locomotive.SteamLocomotiveContainer;
import xyz.brassgoggledcoders.transport.container.provider.EntityContainerProvider;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.engine.SteamEngine;
import xyz.brassgoggledcoders.transport.util.LazyOptionalHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class SteamLocomotiveEntity extends LocomotiveEntity<SteamEngine> {
    public SteamLocomotiveEntity(EntityType<? extends SteamLocomotiveEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getEntityWorld().isRemote() && this.isPowered() && this.rand.nextInt(4) == 0) {
            double clientRadians = Math.toRadians(this.getClientAngle());
            this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosX() - 0.9D * Math.cos(clientRadians),
                    this.getPosY() + 2.2D, this.getPosZ() - 0.9D * Math.sin(clientRadians), 0.0D,
                    0.0D, 0.0D);
        }
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (!player.isCrouching()) {
            if (!FluidUtil.interactWithFluidHandler(player, hand, this.getEngine().getWaterTank())) {
                if (player instanceof ServerPlayerEntity) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new EntityContainerProvider<>(this,
                            SteamLocomotiveContainer::new));
                }
            }
            return ActionResultType.func_233537_a_(this.getEntityWorld().isRemote());
        } else {
            return super.processInitialInteract(player, hand);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptionalHelper.getOrElse(
                this.getEngine().getCapability(cap, side),
                () -> super.getCapability(cap, side)
        );
    }

    @Override
    public void invalidateCaps() {
        this.getEngine().invalidateCaps();
        super.invalidateCaps();
    }

    @Override
    public SteamEngine createEngine() {
        return new SteamEngine(this::isOn);
    }

    @Nonnull
    @Override
    public ItemStack createItemStack() {
        return new ItemStack(TransportEntities.STEAM_LOCOMOTIVE_ITEM.get());
    }
}
