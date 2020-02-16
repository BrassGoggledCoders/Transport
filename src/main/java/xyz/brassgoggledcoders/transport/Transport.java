package xyz.brassgoggledcoders.transport;

import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.CargoCarrierEmpty;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.item.TransportItemGroup;
import xyz.brassgoggledcoders.transport.nbt.NBTStorage;

import static xyz.brassgoggledcoders.transport.Transport.ID;

@Mod(ID)
public class Transport {
    public static final String ID = "transport";

    public static Transport instance;

    public final ItemGroup transportGroup;

    public Transport() {
        instance = this;

        this.transportGroup = new TransportItemGroup("transport", TransportBlocks.HOLDING_RAIL_ITEM::get);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);

        TransportBlocks.register(modBus);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ICargoCarrier.class, new NBTStorage<>(), CargoCarrierEmpty::new);
    }
}
