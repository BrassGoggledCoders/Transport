package xyz.brassgoggledcoders.transport;

import com.teamacronymcoders.base.BaseModFoundation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;
import xyz.brassgoggledcoders.transport.api.cargo.CargoRegistry;
import xyz.brassgoggledcoders.transport.api.registry.TransportRegisterEvent;
import xyz.brassgoggledcoders.transport.proxy.IProxy;

import static xyz.brassgoggledcoders.transport.Transport.*;

@Mod(modid = ID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class Transport extends BaseModFoundation<Transport> {
    public static final String ID = "transport";
    public static final String NAME = "Transport";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:base@[0.0.0,)";

    @SidedProxy(clientSide = "xyz.brassgoggledcoders.transport.proxy.ClientProxy",
            serverSide = "xyz.brassgoggledcoders.transport.proxy.ServerProxy")
    public static IProxy proxy;

    @Instance
    public static Transport instance;

    public Transport() {
        super(ID, NAME, VERSION, CreativeTabs.TRANSPORTATION);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TransportAPI.setCargoRendererLoader(proxy::getCargoRenderer);
        TransportAPI.setLangHandler(proxy::format);
        CapabilityCargo.register();
        super.preInit(event);
        proxy.setupModelLoader();
        this.getLibProxy().addSidedBlockDomain();
        MinecraftForge.EVENT_BUS.post(new TransportRegisterEvent<>(TransportAPI.getCargoRegistry(), CargoRegistry.class));
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public Transport getInstance() {
        return this;
    }
}
