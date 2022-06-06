package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.signal.SignalLevelData;

@Mod.EventBusSubscriber(modid = Transport.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SignalEventHandler {
    @SubscribeEvent
    public static void tickWorld(TickEvent.WorldTickEvent worldTickEvent) {
        if (worldTickEvent.phase == TickEvent.Phase.END) {
            SignalLevelData.getFor(worldTickEvent.world)
                    .ifPresent(SignalLevelData::tick);
        }
    }

    @SubscribeEvent
    public static void unloadChunk(ChunkEvent.Unload chunkEvent) {
        SignalLevelData.getFor(chunkEvent.getWorld())
                .ifPresent(signalLevelData -> signalLevelData.onChunkUnload(chunkEvent.getChunk()));
    }
}
