package xyz.brassgoggledcoders.transport.api.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

public class ItemModuleRenderer implements IModuleRenderer{
    @Override
    public void render(ModuleInstance<?> moduleInstance, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int packedLight) {
        Minecraft.getInstance().getItemRenderer().renderItem(moduleInstance.asItemStack(), TransformType.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
    }
}
