package xyz.brassgoggledcoders.transport.minecart;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.EntityRegistry;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.minecart.entity.EntityMinecartCargoCarrier;
import xyz.brassgoggledcoders.transport.minecart.item.ItemMinecartCargoCarrier;

@Module(Transport.ID)
public class MinecartModule extends ModuleBase {
    @Override
    public String getName() {
        return "Minecarts";
    }

    @Override
    public void registerEntities(ConfigRegistry configRegistry, EntityRegistry entityRegistry) {
        super.registerEntities(configRegistry, entityRegistry);
        entityRegistry.register(EntityMinecartCargoCarrier.class);
    }

    @Override
    public void registerItems(ConfigRegistry configRegistry, ItemRegistry itemRegistry) {
        super.registerItems(configRegistry, itemRegistry);
        ItemMinecartCargoCarrier itemMinecartCargoCarrier = new ItemMinecartCargoCarrier();
        itemRegistry.register(itemMinecartCargoCarrier);
        Transport.proxy.setItemRenderer(itemMinecartCargoCarrier,
                "xyz.brassgoggledcoders.transport.minecart.render.RenderItemMinecartCargoCarrier");
    }
}
