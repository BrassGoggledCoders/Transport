package xyz.brassgoggledcoders.transport.friends.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import xyz.brassgoggledcoders.transport.friends.TransportFriends;
import xyz.brassgoggledcoders.transport.friends.data.shellcontent.TransportFriendsShellContentDataProvider;

@EventBusSubscriber(modid = TransportFriends.ID, bus = Bus.MOD)
public class DataEventHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            event.getGenerator().addProvider(new TransportFriendsShellContentDataProvider(event.getGenerator()));
        }
    }
}
