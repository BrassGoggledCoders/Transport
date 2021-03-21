package xyz.brassgoggledcoders.transport.renderer.boat;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;

public class DieselTugBoatRenderer extends TugBoatRenderer<TugBoatEntity> {
    private static final ResourceLocation NAME = Transport.rl("diesel_boat");

    public DieselTugBoatRenderer(EntityRendererManager renderManager) {
        super(NAME, renderManager);
    }
}
