package xyz.brassgoggledcoders.transport.library.render.cargo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

@SuppressWarnings("unused")
public class CargoBlockRenderer implements ICargoRenderer {
    private final IBlockState blockState;

    public CargoBlockRenderer(IBlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public void render(ICargoInstance cargo, ICargoCarrier cargoCarrier, float partialTicks) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(blockState, cargoCarrier.getBrightness());
        GlStateManager.popMatrix();
    }
}
