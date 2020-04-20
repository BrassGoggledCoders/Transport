package xyz.brassgoggledcoders.transport.api.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

public interface IModuleRenderer {
    void render(ModuleInstance<?> moduleInstance, float entityYaw, float partialTicks, MatrixStack matrixStack,
                IRenderTypeBuffer buffer, int packedLight);

}
