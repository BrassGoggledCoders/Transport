package xyz.brassgoggledcoders.transport.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorageProvider;
import xyz.brassgoggledcoders.transport.api.transfer.EnergyTransferor;
import xyz.brassgoggledcoders.transport.api.transfer.FluidTransferor;
import xyz.brassgoggledcoders.transport.api.transfer.ITransferor;
import xyz.brassgoggledcoders.transport.api.transfer.ItemTransferor;
import xyz.brassgoggledcoders.transport.capability.itemhandler.furnaceminecart.FurnaceMinecartFuelProvider;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Transport.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        if (entityAttachCapabilitiesEvent.getObject() instanceof FurnaceMinecartEntity) {
            entityAttachCapabilitiesEvent.addCapability(new ResourceLocation(Transport.ID, "furnace_fuel"),
                    new FurnaceMinecartFuelProvider((FurnaceMinecartEntity) entityAttachCapabilitiesEvent.getObject()));
        }
    }

    @SubscribeEvent
    public static void onAttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent) {
        if (attachCapabilitiesEvent.getObject() instanceof LecternTileEntity) {
            attachCapabilitiesEvent.addCapability(new ResourceLocation(Transport.ID, "predicate_storage"),
                    new PredicateStorageProvider());
        }
    }
}
