package xyz.brassgoggledcoders.transport.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.function.Function;

public class TransportTOP implements Function<ITheOneProbe, Void> {

    public TransportTOP(IEventBus modBus) {
        modBus.addListener(this::sendIMC);
    }

    public void sendIMC(InterModEnqueueEvent enqueueEvent) {
        InterModComms.sendTo(
                "theoneprobe",
                "getTheOneProbe",
                () -> this
        );
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerEntityProvider(new ShellInfoEntityProvider(theOneProbe.createProbeConfig()));
        return null;
    }
}
