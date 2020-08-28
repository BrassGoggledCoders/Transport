package xyz.brassgoggledcoders.transport.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.tileentity.EnchantmentTableTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;
import xyz.brassgoggledcoders.transport.block.PodiumBlock;
import xyz.brassgoggledcoders.transport.tileentity.PodiumTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class PodiumTileEntityRenderer extends TileEntityRenderer<PodiumTileEntity> {
    private final BookModel bookModel = new BookModel();

    public PodiumTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(PodiumTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer,
                       int combinedLight, int combinedOverlay) {
        BlockState blockstate = tileEntity.getBlockState();
        if (blockstate.get(PodiumBlock.HAS_ITEM)) {
            if (tileEntity.renderBook()) {
                matrixStack.push();
                matrixStack.translate(0.5D, 1.0625D, 0.5D);
                float f = blockstate.get(PodiumBlock.FACING).rotateY().getHorizontalAngle();
                matrixStack.rotate(Vector3f.YP.rotationDegrees(-f));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(67.5F));
                matrixStack.translate(0.0D, -0.125D, 0.0D);
                this.bookModel.setBookState(0.0F, 0.1F, 0.9F, 1.2F);
                IVertexBuilder ivertexbuilder = EnchantmentTableTileEntityRenderer.TEXTURE_BOOK.getBuffer(buffer,
                        RenderType::getEntitySolid);
                this.bookModel.renderAll(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F,
                        1.0F, 1.0F, 1.0F);
                matrixStack.pop();
            }
        }
    }
}
