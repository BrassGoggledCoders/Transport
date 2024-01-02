package xyz.brassgoggledcoders.transport.compat.top.service;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public class EnergyTOPShellProvider implements ITOPShellProvider {
    @Override
    public void addInfo(ShellContent shellContent, ProbeMode mode, IProbeInfo probeInfo, IProbeConfig probeConfig) {
        shellContent.getCapability(ForgeCapabilities.ENERGY)
                .ifPresent(iEnergyStorage -> addEnergyInfo(
                        probeInfo,
                        probeConfig,
                        iEnergyStorage.getEnergyStored(),
                        iEnergyStorage.getMaxEnergyStored()
                ));
    }

    private void addEnergyInfo(IProbeInfo probeInfo, IProbeConfig config, long energy, long maxEnergy) {
        if (config.getRFMode() == 1) {
            probeInfo.progress(
                    energy,
                    maxEnergy,
                    probeInfo.defaultProgressStyle()
                            .suffix("FE")
                            .filledColor(Config.rfbarFilledColor)
                            .alternateFilledColor(Config.rfbarAlternateFilledColor)
                            .borderColor(Config.rfbarBorderColor)
                            .numberFormat(Config.rfFormat.get()));
        } else {
            probeInfo.text(CompoundText.create()
                    .style(TextStyleClass.PROGRESS)
                    .text("FE: " + ElementProgress.format(
                            energy,
                            Config.rfFormat.get(),
                            Component.literal("FE")
                    ))
            );
        }

    }
}
