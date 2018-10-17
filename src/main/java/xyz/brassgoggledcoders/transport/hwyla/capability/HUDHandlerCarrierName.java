package xyz.brassgoggledcoders.transport.hwyla.capability;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;

import javax.annotation.Nonnull;
import java.util.List;

public class HUDHandlerCarrierName implements IWailaEntityProvider {
    public static final HUDHandlerCarrierName INSTANCE = new HUDHandlerCarrierName();

    @Nonnull
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        currenttip.remove("§r§fentity.entityminecartcargocarrier.name");
        ICargoCarrier cargoCarrier = entity.getCapability(CapabilityCargo.CARRIER, null);
        if (cargoCarrier != null) {
            String displayName = "";

            displayName += I18n.format(Items.MINECART.getUnlocalizedName() + ".name");
            displayName += " " + I18n.format("transport.separator.with") + " ";
            displayName += cargoCarrier.getCargoInstance().getLocalizedName();

            currenttip.add(displayName);
        }
        return currenttip;
    }
}
