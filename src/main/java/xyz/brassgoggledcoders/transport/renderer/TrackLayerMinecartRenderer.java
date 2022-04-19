package xyz.brassgoggledcoders.transport.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.TrackLayerMinecart;
import xyz.brassgoggledcoders.transport.model.TrackLayerModel;

import javax.annotation.ParametersAreNonnullByDefault;

public class TrackLayerMinecartRenderer extends BasicMinecartRenderer<TrackLayerMinecart> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Transport.rl("track_layer"), "minecart");

    private final EntityModel<TrackLayerMinecart> trackLayerModel;

    public TrackLayerMinecartRenderer(EntityRendererProvider.Context context) {
        super(context, LAYER_LOCATION, false);
        this.trackLayerModel = new TrackLayerModel<>(context.bakeLayer(TrackLayerModel.LAYER_LOCATION));
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderMinecartContents(TrackLayerMinecart pEntity, float pPartialTicks, BlockState pState,
                                          PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.trackLayerModel.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.trackLayerModel.renderType(TrackLayerModel.TEXTURE_LOCATION));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        pMatrixStack.translate(0, 0, 0.5);
        this.trackLayerModel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
