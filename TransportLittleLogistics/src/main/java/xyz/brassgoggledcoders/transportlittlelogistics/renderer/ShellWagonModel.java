package xyz.brassgoggledcoders.transportlittlelogistics.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.transportlittlelogistics.entity.ShellWagon;

public class ShellWagonModel extends EntityModel<ShellWagon> {
    private final ModelPart bb_main;

    public ShellWagonModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, -8.0F, 2.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(5.0F, -14.0F, -8.0F, 2.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(0, 28).addBox(-5.0F, -14.0F, -8.0F, 10.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 28).addBox(-5.0F, -14.0F, 6.0F, 10.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(20, 0).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-6.0F, -2.0F, 4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-6.0F, -2.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(5.0F, -2.0F, 4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(5.0F, -2.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void setupAnim(ShellWagon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                          float headPitch) {
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
