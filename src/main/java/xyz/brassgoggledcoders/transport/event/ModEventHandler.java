package xyz.brassgoggledcoders.transport.event;

import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.loading.BlockLoaderRegistry;
import xyz.brassgoggledcoders.transport.api.loading.IEntityBlockLoading;
import xyz.brassgoggledcoders.transport.loading.ContainerMinecartIEntityBlockLoading;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void interModEnqueue(InterModEnqueueEvent event) {
        sendEntityBlockLoaderCom(EntityType.CHEST_MINECART, new ContainerMinecartIEntityBlockLoading(ChestTileEntity::new));
        sendEntityBlockLoaderCom(EntityType.HOPPER_MINECART, new ContainerMinecartIEntityBlockLoading(HopperTileEntity::new));
    }

    @SubscribeEvent
    public static void interModProcess(InterModProcessEvent event) {
        BlockLoaderRegistry blockLoaderRegistry = TransportAPI.getBlockLoadingRegistry();
        forEach(event.getIMCStream("entity_block_loader"::equals), blockLoaderRegistry::registerBlockLoadingFor);
    }

    private static void sendEntityBlockLoaderCom(EntityType<?> entityType, IEntityBlockLoading entityBlockLoading) {
        InterModComms.sendTo(Transport.ID, "entity_block_loader", () -> Pair.of(entityType, entityBlockLoading));
    }

    private static <T, U> void forEach(Stream<InterModComms.IMCMessage> messageStream, BiConsumer<T, U> toEach) {
        messageStream.<Supplier<Pair<T, U>>>map(InterModComms.IMCMessage::getMessageSupplier)
                .map(Supplier::get)
                .forEach(pair -> toEach.accept(pair.getLeft(), pair.getRight()));
    }
}
