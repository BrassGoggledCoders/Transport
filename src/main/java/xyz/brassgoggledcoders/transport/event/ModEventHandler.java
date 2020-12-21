package xyz.brassgoggledcoders.transport.event;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.loading.BlockLoaderRegistry;
import xyz.brassgoggledcoders.transport.api.loading.IBlockEntityLoading;
import xyz.brassgoggledcoders.transport.api.loading.IEntityBlockLoading;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.loading.ContainerMinecartLoading;
import xyz.brassgoggledcoders.transport.loading.ModularVehicleLoading;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void interModEnqueue(InterModEnqueueEvent event) {
        sendLoaderCom(new ContainerMinecartLoading<>(Blocks.CHEST, EntityType.CHEST_MINECART, ChestTileEntity::new));
        sendLoaderCom(new ContainerMinecartLoading<>(Blocks.HOPPER, EntityType.HOPPER_MINECART, HopperTileEntity::new));
        sendLoaderCom(new ModularVehicleLoading(
                TransportEntities.CARGO_MINECART.get(),
                TransportEntities.MODULAR_BOAT.get()
        ));
    }

    @SubscribeEvent
    public static void interModProcess(InterModProcessEvent event) {
        forEach(event.getIMCStream("loading"::equalsIgnoreCase), loading -> {
            BlockLoaderRegistry blockLoaderRegistry = TransportAPI.getBlockLoadingRegistry();
            if (loading instanceof IEntityBlockLoading) {
                IEntityBlockLoading entityBlockLoading = (IEntityBlockLoading) loading;
                for (EntityType<?> entityType : entityBlockLoading.getSupportedEntities()) {
                    blockLoaderRegistry.registerBlockLoadingFor(entityType, entityBlockLoading);
                }
            }
            if (loading instanceof IBlockEntityLoading) {
                IBlockEntityLoading blockEntityLoading = (IBlockEntityLoading) loading;
                for (Block block : blockEntityLoading.getSupportedBlocks()) {
                    blockLoaderRegistry.registerEntityLoadingFor(block, blockEntityLoading);
                }
            }
        });
    }

    private static void sendLoaderCom(Object entityBlockLoading) {
        InterModComms.sendTo(Transport.ID, "loading", () -> entityBlockLoading);
    }

    private static <T> void forEach(Stream<InterModComms.IMCMessage> messageStream, Consumer<T> toEach) {
        messageStream.<Supplier<T>>map(InterModComms.IMCMessage::getMessageSupplier)
                .map(Supplier::get)
                .forEach(toEach);
    }
}
