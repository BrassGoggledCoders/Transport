package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.container.locomotive.SteamLocomotiveContainer;
import xyz.brassgoggledcoders.transport.container.provider.EntityContainerProvider;
import xyz.brassgoggledcoders.transport.engine.Engine;
import xyz.brassgoggledcoders.transport.engine.SteamEngine;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SteamLocomotiveEntity extends LocomotiveEntity<SteamEngine> {
    public SteamLocomotiveEntity(EntityType<? extends SteamLocomotiveEntity> type, World world) {
        super(type, world);
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

    @Override
    public SteamEngine createEngine() {
        return new SteamEngine(this::isOn);
    }
}
