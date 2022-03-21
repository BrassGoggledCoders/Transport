package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.service.ServiceHolder;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.FORGE)
public class ForgeCommonEventHandler {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        if (ServiceHolder.SHELL_CONTENT_CREATOR.get() instanceof PreparableReloadListener reloadListener) {
            event.addListener(reloadListener);
        }
    }
}
