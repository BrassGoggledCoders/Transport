package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModCommonEventHandler {

    @SubscribeEvent
    public static void capabilityRegister(RegisterCapabilitiesEvent event) {
        event.register(IRailProvider.class);
    }
}
