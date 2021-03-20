package xyz.brassgoggledcoders.transport.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorageProvider;
import xyz.brassgoggledcoders.transport.capability.itemhandler.furnaceminecart.FurnaceMinecartFuelProvider;

@EventBusSubscriber(modid = Transport.ID, bus = EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    public static final ResourceLocation FURNACE_FUEL = Transport.rl("furnace_fuel");
    public static final ResourceLocation PREDICATE_STORAGE = Transport.rl("predicate_storage");
    public static final ResourceLocation NAVIGATION_NETWORK = Transport.rl("navigation_network");

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        if (entityAttachCapabilitiesEvent.getObject() instanceof FurnaceMinecartEntity) {
            FurnaceMinecartFuelProvider fuelProvider = new FurnaceMinecartFuelProvider(
                    (FurnaceMinecartEntity) entityAttachCapabilitiesEvent.getObject());
            entityAttachCapabilitiesEvent.addCapability(FURNACE_FUEL, fuelProvider);
            entityAttachCapabilitiesEvent.addListener(fuelProvider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onAttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent) {
        if (attachCapabilitiesEvent.getObject() instanceof LecternTileEntity) {
            PredicateStorageProvider storageProvider = new PredicateStorageProvider();
            attachCapabilitiesEvent.addCapability(PREDICATE_STORAGE, storageProvider);
            attachCapabilitiesEvent.addListener(storageProvider::invalidate);
        }
    }
}
