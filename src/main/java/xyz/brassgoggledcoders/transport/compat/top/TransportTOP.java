package xyz.brassgoggledcoders.transport.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

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
