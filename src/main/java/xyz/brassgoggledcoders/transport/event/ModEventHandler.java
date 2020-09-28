package xyz.brassgoggledcoders.transport.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.transfer.EnergyTransferor;
import xyz.brassgoggledcoders.transport.api.transfer.FluidTransferor;
import xyz.brassgoggledcoders.transport.api.transfer.ITransferor;
import xyz.brassgoggledcoders.transport.api.transfer.ItemTransferor;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModEventHandler {
    private final static String TRANSFERORS = "transferors";

    @SubscribeEvent
    public static void imcEnqueue(InterModEnqueueEvent enqueueEvent) {
        InterModComms.sendTo(Transport.ID, TRANSFERORS, ItemTransferor::new);
        InterModComms.sendTo(Transport.ID, TRANSFERORS, FluidTransferor::new);
        InterModComms.sendTo(Transport.ID, TRANSFERORS, EnergyTransferor::new);
    }

    @SubscribeEvent
    public static void imcProcess(InterModProcessEvent processEvent) {
        processEvent.getIMCStream(TRANSFERORS::equalsIgnoreCase)
                .map(InterModComms.IMCMessage::<ITransferor<?>>getMessageSupplier)
                .map(Supplier::get)
                .forEach(TransportAPI::registerTransferor);
    }
}
