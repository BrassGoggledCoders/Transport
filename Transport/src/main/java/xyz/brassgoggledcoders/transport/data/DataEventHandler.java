package xyz.brassgoggledcoders.transport.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.data.shellcontent.TransportShellContentDataProvider;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class DataEventHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            event.getGenerator().addProvider(new TransportShellContentDataProvider(event.getGenerator()));
        }
    }
}
