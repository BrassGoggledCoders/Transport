package xyz.brassgoggledcoders.transport.compat.top;

import com.google.common.base.Suppliers;
import mcjty.theoneprobe.api.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.compat.top.service.ITOPShellProvider;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public class ShellInfoEntityProvider implements IProbeInfoEntityProvider {
    public static String ID = Transport.rl("shell_info")
            .toString();

    private final Supplier<ServiceLoader<ITOPShellProvider>> serviceLoader = Suppliers.memoize(() -> ServiceLoader.load(ITOPShellProvider.class));

    private final IProbeConfig probeConfig;

    public ShellInfoEntityProvider(IProbeConfig probeConfig) {
        this.probeConfig = probeConfig;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof IShell shell) {
            ShellContent shellContent = shell.getContent();

            for (ITOPShellProvider provider : serviceLoader.get()) {
                provider.addInfo(shellContent, probeMode, iProbeInfo, probeConfig);
            }

            shellContent.getCapability(ForgeCapabilities.FLUID_HANDLER)
                    .map(TankReference::createHandler)
                    .ifPresent(iProbeInfo::tank);
        }
    }
}
