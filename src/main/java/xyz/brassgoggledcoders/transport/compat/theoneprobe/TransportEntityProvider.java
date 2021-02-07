package xyz.brassgoggledcoders.transport.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.lookingat.ILookingAt;

import static mcjty.theoneprobe.api.TextStyleClass.PROGRESS;

public class TransportEntityProvider implements IProbeInfoEntityProvider {
    private static final String ID = Transport.ID + ":entity";

    private final IProbeConfig probeConfig;


    public TransportEntityProvider(IProbeConfig probeConfig) {
        this.probeConfig = probeConfig;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity playerEntity, World world,
                                   Entity entity, IProbeHitEntityData probeHitEntityData) {
        LazyOptional<IModularEntity> modularEntityLazyOptional = entity.getCapability(TransportAPI.MODULAR_ENTITY);
        if (modularEntityLazyOptional.isPresent()) {
            IModularEntity modularEntity = modularEntityLazyOptional.orElseThrow(() -> new IllegalStateException(
                    "Found no Modular Entity"));
        } else if (entity instanceof ILookingAt) {

        } else if (entity instanceof AbstractMinecartEntity || entity instanceof BoatEntity) {
            if (TransportTOP.show(probeMode, probeConfig.getShowChestContents())) {
                entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .ifPresent(itemHandler -> {
                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                ItemStack itemStack = itemHandler.getStackInSlot(i);
                                if (!itemStack.isEmpty()) {
                                    probeInfo.item(itemStack);
                                }
                            }
                        });
            }

            if (this.probeConfig.getRFMode() > 0) {
                entity.getCapability(CapabilityEnergy.ENERGY)
                        .ifPresent(energy -> {
                            if (probeConfig.getRFMode() == 1) {
                                probeInfo.progress(energy.getEnergyStored(), energy.getMaxEnergyStored(),
                                        probeInfo.defaultProgressStyle()
                                                .suffix("FE")
                                                .numberFormat(NumberFormat.COMPACT)
                                );
                            } else {
                                probeInfo.text(CompoundText.create().style(PROGRESS).text("FE: " +
                                        TheOneProbeHelper.format(energy.getEnergyStored(), NumberFormat.FULL, "FE")));
                            }
                        });
            }
        }
    }
}
