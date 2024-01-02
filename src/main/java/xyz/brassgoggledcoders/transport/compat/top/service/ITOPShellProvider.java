package xyz.brassgoggledcoders.transport.compat.top.service;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

public interface ITOPShellProvider {
    void addInfo(ShellContent shellContent, ProbeMode mode, IProbeInfo probeInfo, IProbeConfig probeConfig);
}
