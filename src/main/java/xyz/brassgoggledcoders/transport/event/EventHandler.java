package xyz.brassgoggledcoders.transport.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.master.Manageable;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorageProvider;
import xyz.brassgoggledcoders.transport.capability.NBTCapabilityProvider;
import xyz.brassgoggledcoders.transport.capability.itemhandler.furnaceminecart.FurnaceMinecartFuelProvider;

public class EventHandler {
    public static final ResourceLocation MANAGEABLE = Transport.rl("manageable");

    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> entityAttachCapabilitiesEvent) {
        if (entityAttachCapabilitiesEvent.getObject() instanceof FurnaceMinecartEntity) {
            entityAttachCapabilitiesEvent.addCapability(new ResourceLocation(Transport.ID, "furnace_fuel"),
                    new FurnaceMinecartFuelProvider((FurnaceMinecartEntity) entityAttachCapabilitiesEvent.getObject()));
        }
    }

    public static void onAttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> attachCapabilitiesEvent) {
        if (attachCapabilitiesEvent.getObject() instanceof LecternTileEntity) {
            attachCapabilitiesEvent.addCapability(new ResourceLocation(Transport.ID, "predicate_storage"),
                    new PredicateStorageProvider());
        }

        if (!attachCapabilitiesEvent.getCapabilities().containsKey(MANAGEABLE)) {
            attachCapabilitiesEvent.addCapability(MANAGEABLE, new NBTCapabilityProvider<>(TransportAPI.MANAGEABLE,
                    new Manageable(null)));
        }
    }
}
