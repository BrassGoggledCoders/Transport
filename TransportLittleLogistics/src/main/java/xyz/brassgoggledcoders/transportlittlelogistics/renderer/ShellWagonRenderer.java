package xyz.brassgoggledcoders.transportlittlelogistics.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.murad.shipping.entity.custom.vessel.barge.FluidTankBargeEntity;
import dev.murad.shipping.entity.render.train.TrainCarRenderer;
import dev.murad.shipping.util.FluidRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import xyz.brassgoggledcoders.transportlittlelogistics.TransportLittleLogistics;
import xyz.brassgoggledcoders.transportlittlelogistics.entity.ShellWagon;

import javax.annotation.ParametersAreNonnullByDefault;

public class ShellWagonRenderer extends TrainCarRenderer<ShellWagon> {
    public static final ModelLayerLocation SHELL_WAGON_LOCATION = new ModelLayerLocation(
            TransportLittleLogistics.rl("shell_wagon"),
            "main"
    );

    public ShellWagonRenderer(EntityRendererProvider.Context context) {
        super(context, ShellWagonModel::new, SHELL_WAGON_LOCATION, "textures/entity/chest_car.png");
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    protected void renderAdditional(ShellWagon entity, float pEntityYaw, float partialTicks, PoseStack matrixStackIn,
                                    MultiBufferSource bufferIn, int pPackedLight) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.ZN.rotationDegrees(180.0F));
        matrixStackIn.translate(-0.30, -1.05, -0.30);
        matrixStackIn.scale(0.60F, 0.60F, 0.60F);

        Minecraft.getInstance()
                .getBlockRenderer()
                .renderSingleBlock(
                        entity.getDisplayBlockState(),
                        matrixStackIn,
                        bufferIn,
                        pPackedLight,
                        OverlayTexture.NO_OVERLAY
                );
        matrixStackIn.popPose();
    }
}
