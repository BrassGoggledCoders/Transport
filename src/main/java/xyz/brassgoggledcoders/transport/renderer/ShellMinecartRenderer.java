package xyz.brassgoggledcoders.transport.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.ShellMinecart;

import javax.annotation.ParametersAreNonnullByDefault;

public class ShellMinecartRenderer extends MinecartRenderer<ShellMinecart> {
    public static final ModelLayerLocation SHELL_MINECART_LOCATION = new ModelLayerLocation(
            Transport.rl("shell_minecart"),
            "main"
    );

    public ShellMinecartRenderer(EntityRendererProvider.Context context) {
        super(context, SHELL_MINECART_LOCATION);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderMinecartContents(ShellMinecart pEntity, float pPartialTicks, BlockState pState, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(pState, pMatrixStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY);
    }
}
