package xyz.brassgoggledcoders.transport.api.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

public class CargoModuleRender implements IModuleRenderer {
    @Override
    public void render(ModuleInstance<?> moduleInstance, float entityYaw, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int packedLight) {
        if (moduleInstance instanceof CargoModuleInstance) {
            BlockState blockState = ((CargoModuleInstance) moduleInstance).getBlockState();
            if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                matrixStack.scale(0.75F, 0.75F, 0.75F);
                matrixStack.translate(-0.5D, -0.125, 0.5D);
                matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
                this.renderBlockState(blockState, matrixStack, buffer, packedLight);
            }
        }

    }

    protected void renderBlockState(BlockState blockState, MatrixStack matrixStack, IRenderTypeBuffer buffer,
                                    int packedLight) {
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockState, matrixStack, buffer, packedLight,
                OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
    }
}
