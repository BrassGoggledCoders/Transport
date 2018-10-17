package xyz.brassgoggledcoders.transport.hwyla;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.Entity;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.hwyla.capability.HUDHandlerCarrierName;
import xyz.brassgoggledcoders.transport.hwyla.capability.HUDHandlerEntityEnergy;
import xyz.brassgoggledcoders.transport.minecart.entity.EntityMinecartCargoCarrier;

@WailaPlugin
public class HwylaRegister implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        if (Transport.instance.getModuleHandler().isModuleEnabled("Hwyla")) {
            registrar.addConfig("Transport", "transport.entity.energy");

            registrar.registerBodyProvider(HUDHandlerEntityEnergy.INSTANCE, Entity.class);
            registrar.registerNBTProvider(HUDHandlerEntityEnergy.INSTANCE, Entity.class);

            registrar.registerHeadProvider(HUDHandlerCarrierName.INSTANCE, EntityMinecartCargoCarrier.class);
        }
    }
}
