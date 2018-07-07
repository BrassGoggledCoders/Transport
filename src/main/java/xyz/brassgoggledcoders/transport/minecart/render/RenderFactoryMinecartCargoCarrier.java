package xyz.brassgoggledcoders.transport.minecart.render;

import com.teamacronymcoders.base.renderer.entity.loader.EntityRenderer;
import com.teamacronymcoders.base.renderer.entity.loader.IEntityRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.minecart.entity.EntityMinecartCargoCarrier;

@EntityRenderer(module = "Minecarts", handler = Transport.ID)
public class RenderFactoryMinecartCargoCarrier implements IEntityRenderer<EntityMinecartCargoCarrier> {
    @Override
    public Class<EntityMinecartCargoCarrier> getEntityClass() {
        return EntityMinecartCargoCarrier.class;
    }

    @Override
    public IRenderFactory getRenderFactory() {
        return RenderMinecartCargoCarrier::new;
    }
}
