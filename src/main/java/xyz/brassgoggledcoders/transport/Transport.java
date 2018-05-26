package xyz.brassgoggledcoders.transport;

import com.teamacronymcoders.base.BaseModFoundation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static xyz.brassgoggledcoders.transport.Transport.*;

@Mod(modid = ID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class Transport extends BaseModFoundation<Transport> {
    public static final String ID = "transport";
    public static final String NAME = "Transport";
    public static final String VERSION = "@@VERSION@@";
    public static final String DEPENDENCIES = "required-after:base@[0.0.0,)";

    public Transport() {
        super(ID, NAME, VERSION, CreativeTabs.TRANSPORTATION);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        this.getLibProxy().addSidedBlockDomain();
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
