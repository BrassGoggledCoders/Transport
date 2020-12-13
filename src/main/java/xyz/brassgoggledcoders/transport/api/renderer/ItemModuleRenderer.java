package xyz.brassgoggledcoders.transport.api.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Quaternion;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

public class ItemModuleRenderer implements IModuleRenderer {
    @Override
    public void render(ModuleInstance<?> moduleInstance, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.rotate(new Quaternion(0, 0, -90, true));
        matrixStack.translate(-0.125, -0.125, 0);
        Minecraft.getInstance().getItemRenderer().renderItem(moduleInstance.asItemStack(), TransformType.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
    }
}
