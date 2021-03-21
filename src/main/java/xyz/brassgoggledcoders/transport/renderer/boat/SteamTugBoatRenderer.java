package xyz.brassgoggledcoders.transport.renderer.boat;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.TugBoatEntity;

public class SteamTugBoatRenderer extends TugBoatRenderer<TugBoatEntity> {
    private static final ResourceLocation NAME = Transport.rl("steam_boat");

    public SteamTugBoatRenderer(EntityRendererManager renderManager) {
        super(NAME, renderManager);
    }
}
