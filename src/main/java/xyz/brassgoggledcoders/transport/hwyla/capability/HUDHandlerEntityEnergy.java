package xyz.brassgoggledcoders.transport.hwyla.capability;

import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.List;

public class HUDHandlerEntityEnergy implements IWailaEntityProvider {

    public static final HUDHandlerEntityEnergy INSTANCE = new HUDHandlerEntityEnergy();

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currentTip, IWailaEntityAccessor accessor, IWailaConfigHandler config) { ;
        if (config.getConfig("transport.entity.energy") && accessor.getNBTData().hasKey("forgeEnergy")) {
            NBTTagCompound energyTag = accessor.getNBTData().getCompoundTag("forgeEnergy");
            int stored = energyTag.getInteger("stored");
            int capacity = energyTag.getInteger("capacity");

            ((ITaggedList<String, String>) currentTip).add(String.format("%d / %d FE", stored, capacity), "IEnergyStorage");
            return currentTip;
        }

        return currentTip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity entity, NBTTagCompound tag, World world)  {
        if (entity != null) {
            IEnergyStorage energyStorage = entity.getCapability(CapabilityEnergy.ENERGY, null);
            if (energyStorage != null) {
                NBTTagCompound energyTag = new NBTTagCompound();
                energyTag.setInteger("capacity", energyStorage.getMaxEnergyStored());
                energyTag.setInteger("stored", energyStorage.getEnergyStored());
                tag.setTag("forgeEnergy", energyTag);
            }
        }
        return tag;
    }
}
