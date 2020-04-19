package xyz.brassgoggledcoders.transport.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.block.ModuleConfiguratorBlock;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class ModuleConfiguratorTileEntityRenderer extends TileEntityRenderer<ModuleConfiguratorTileEntity> {
    public ModuleConfiguratorTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(ModuleConfiguratorTileEntity tileEntity, float partialTicks, MatrixStack matrixStack,
                       IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        IModularEntity modularEntity = tileEntity.getEntity();
        if (modularEntity != null) {
            matrixStack.push();
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            matrixStack.translate(0.675F, 0.6F, 0.675F);
            matrixStack.rotate(new Quaternion(0, tileEntity.getBlockState().get(ModuleConfiguratorBlock.FACING)
                    .getHorizontalIndex() * 90, 0, true));
            Minecraft.getInstance().getRenderManager().renderEntityStatic(modularEntity.getSelf(), 0, 0, 0,
                    0, partialTicks, matrixStack, buffer, combinedLight);
            matrixStack.pop();
        }
    }
}
