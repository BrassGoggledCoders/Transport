package xyz.brassgoggledcoders.transport.minecart.render;

import com.teamacronymcoders.base.util.CapUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.cargo.CapabilityCargo;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

public class RenderItemMinecartCargoCarrier extends TileEntityItemStackRenderer {
    private final ModelMinecart modelMinecart = new ModelMinecart();
    private final ResourceLocation minecartTexture = new ResourceLocation("textures/entity/minecart.png");

    public void renderByItem(ItemStack itemStack, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(.5, .55, .5);
        switch (TESRBakedModel.transformType) {
            case GROUND:
                GlStateManager.scale(.15, .15, .15);
                break;
            case GUI:
                GlStateManager.scale(.35, .35, .35);
                GlStateManager.rotate(45, 1, 0, 0);
                GlStateManager.rotate(45, 0, -1, 0);
                GlStateManager.translate(0, -0.25, 0);
                break;
            case FIRST_PERSON_LEFT_HAND:
                GlStateManager.scale(.2, .2, .2);
                GlStateManager.rotate(45, 1, 0, 0);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                GlStateManager.scale(.2, .2, .2);
                GlStateManager.rotate(45, 1, 0, 0);
                break;
            case THIRD_PERSON_LEFT_HAND:
                GlStateManager.scale(.15, .15, .15);
                GlStateManager.rotate(45, 1, 0, 0);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                GlStateManager.scale(.15, .15, .15);
                GlStateManager.rotate(45, 1, 0, 0);
                break;
            default:
                break;
        }
        renderMinecart();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.scale(1.25, 1.25, 1.25);
        GlStateManager.translate(-.5, -0.15, .5);
        CapUtils.getOptional(itemStack, CapabilityCargo.CARRIER).ifPresent(cargoCarrier ->
                cargoCarrier.getCargoInstance().getCargoRenderer().render(cargoCarrier.getCargoInstance(), cargoCarrier, partialTicks)
        );
        GlStateManager.popMatrix();
    }

    protected void renderMinecart() {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.rotate(90, 0, 1, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(minecartTexture);
        modelMinecart.render(null, 0, 0, 0, 0, 0, 0.1F);
        GlStateManager.popMatrix();
    }
}
