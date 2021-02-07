package xyz.brassgoggledcoders.transport.compat.theoneprobe;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;

public class TransportTOP implements Function<ITheOneProbe, Void> {

    public TransportTOP() {
        FMLJavaModLoadingContext.get()
                .getModEventBus()
                .addListener(this::enqueueIMC);
    }

    public void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> this);
    }

    public Void apply(ITheOneProbe theOneProbe) {
        IProbeConfig probeConfig = theOneProbe.createProbeConfig();
        theOneProbe.registerEntityProvider(new TransportEntityProvider(probeConfig));
        return null;
    }

    public static boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == IProbeConfig.ConfigMode.NORMAL || cfg == IProbeConfig.ConfigMode.EXTENDED && mode == ProbeMode.EXTENDED;
    }
}
