package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
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

    @SubscribeEvent
    public static void entityLeaveLevel(EntityLeaveWorldEvent event) {
        if (event.getEntity() instanceof AbstractMinecart minecart) {
            SignalLevelData.getFor(event.getWorld())
                    .ifPresent(signalLevelData -> signalLevelData.onEntityLeave(minecart));
        }
    }
}
