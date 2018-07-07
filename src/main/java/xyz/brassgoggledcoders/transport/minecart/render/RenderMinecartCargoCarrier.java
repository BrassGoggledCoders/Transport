package xyz.brassgoggledcoders.transport.minecart.render;

import com.teamacronymcoders.base.renderer.entity.loader.EntityRenderer;
import com.teamacronymcoders.base.renderer.entity.minecart.RenderMinecartBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;
import xyz.brassgoggledcoders.transport.minecart.entity.EntityMinecartCargoCarrier;

public class RenderMinecartCargoCarrier extends RenderMinecartBase<EntityMinecartCargoCarrier> {

    public RenderMinecartCargoCarrier(RenderManager renderManager) {
        super(renderManager);
    }

    protected void renderBlock(EntityMinecartCargoCarrier entity, float partialTicks) {
        ICargoCarrier cargoCarrier = entity.getCargoCarrier();
        ICargoInstance cargoInstance = cargoCarrier.getCargoInstance();

        GlStateManager.pushMatrix();
        float scale = 0.75F;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(-0.5F, (float) (6 - 8) / 16.0F, 0.5F);
        cargoInstance.getCargoRenderer().render(cargoInstance, cargoCarrier, partialTicks);
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        cargoInstance.getCargoRenderer().render(cargoInstance, cargoCarrier, partialTicks);
    }
}
