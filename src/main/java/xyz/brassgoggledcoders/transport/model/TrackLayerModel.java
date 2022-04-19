package xyz.brassgoggledcoders.transport.model;// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import xyz.brassgoggledcoders.transport.Transport;

import javax.annotation.Nonnull;

public class TrackLayerModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Transport.rl("track_layer"), "main");
    public static final ResourceLocation TEXTURE_LOCATION = Transport.rl("textures/entity/track_layer.png");
    private final ModelPart cartBase;
    private final ModelPart piston;
    private final ModelPart leftBarTop;
    private final ModelPart rightBarBottom;
    private final ModelPart chest;
    private final ModelPart frontLeftBrace;
    private final ModelPart backLeftBrace;
    private final ModelPart frontRightBrace;
    private final ModelPart backRightBrace;
    private final ModelPart topBrace2;
    private final ModelPart topBrace1;
    private final ModelPart hopper1;
    private final ModelPart rightBarTop;
    private final ModelPart leftBarBottom;

    private final ModelPart wheelBackLeft;
    private final ModelPart wheelFrontLeft;
    private final ModelPart wheelBackRight;
    private final ModelPart wheelFrontRight;

    public TrackLayerModel(ModelPart root) {
        this.cartBase = root.getChild("CartBase");
        this.piston = root.getChild("Piston");
        this.leftBarTop = root.getChild("LeftBarTop");
        this.rightBarBottom = root.getChild("RightBarBottom");
        this.chest = root.getChild("Chest");
        this.frontLeftBrace = root.getChild("FrontLeftBrace");
        this.backLeftBrace = root.getChild("BackLeftBrace");
        this.frontRightBrace = root.getChild("FrontRightBrace");
        this.backRightBrace = root.getChild("BackRightBrace");
        this.topBrace2 = root.getChild("TopBrace2");
        this.topBrace1 = root.getChild("TopBrace1");
        this.hopper1 = root.getChild("Hopper1");
        this.rightBarTop = root.getChild("RightBarTop");
        this.leftBarBottom = root.getChild("LeftBarBottom");
        this.wheelBackLeft = root.getChild("WheelBackLeft");
        this.wheelFrontLeft = root.getChild("WheelFrontLeft");
        this.wheelBackRight = root.getChild("WheelBackRight");
        this.wheelFrontRight = root.getChild("WheelFrontRight");
    }

    @Override
    public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        cartBase.render(poseStack, buffer, packedLight, packedOverlay);
        piston.render(poseStack, buffer, packedLight, packedOverlay);
        leftBarTop.render(poseStack, buffer, packedLight, packedOverlay);
        rightBarBottom.render(poseStack, buffer, packedLight, packedOverlay);
        chest.render(poseStack, buffer, packedLight, packedOverlay);
        frontLeftBrace.render(poseStack, buffer, packedLight, packedOverlay);
        backLeftBrace.render(poseStack, buffer, packedLight, packedOverlay);
        frontRightBrace.render(poseStack, buffer, packedLight, packedOverlay);
        backRightBrace.render(poseStack, buffer, packedLight, packedOverlay);
        topBrace2.render(poseStack, buffer, packedLight, packedOverlay);
        topBrace1.render(poseStack, buffer, packedLight, packedOverlay);
        hopper1.render(poseStack, buffer, packedLight, packedOverlay);
        rightBarTop.render(poseStack, buffer, packedLight, packedOverlay);
        leftBarBottom.render(poseStack, buffer, packedLight, packedOverlay);
        wheelBackLeft.render(poseStack, buffer, packedLight, packedOverlay);
        wheelFrontLeft.render(poseStack, buffer, packedLight, packedOverlay);
        wheelBackRight.render(poseStack, buffer, packedLight, packedOverlay);
        wheelFrontRight.render(poseStack, buffer, packedLight, packedOverlay);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("CartBase", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -8.0F, -1.0F, 20.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition piston = partdefinition.addOrReplaceChild("Piston", CubeListBuilder.create().texOffs(60, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(18.0F, -12.0F, 0.0F));
        piston.addOrReplaceChild("PistonHead", CubeListBuilder.create().texOffs(0, 83).addBox(-8.0F, 4.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));
        piston.addOrReplaceChild("PistonHead_1", CubeListBuilder.create().texOffs(36, 37).addBox(-3.0F, 1.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        partdefinition.addOrReplaceChild("LeftBarTop", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 37.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -7.0F, 8.0F));
        partdefinition.addOrReplaceChild("LeftBarBottom", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 37.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -3.0F, 8.0F));

        partdefinition.addOrReplaceChild("RightBarBottom", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, -1.0F, 37.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -3.0F, -9.0F));
        partdefinition.addOrReplaceChild("RightBarTop", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, -1.0F, 37.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -7.0F, -9.0F));

        PartDefinition chest = partdefinition.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(64, 29).addBox(-7.0F, -5.0F, -14.0F, 14.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, -2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition chestLid = chest.addOrReplaceChild("ChestLid", CubeListBuilder.create().texOffs(64, 83).addBox(0.0F, -5.0F, -14.0F, 14.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -5.0F, 0.0F, -0.8652F, 0.0F, 0.0F));
        chestLid.addOrReplaceChild("Latch", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, -3.0F, -15.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("FrontLeftBrace", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(27.0F, -19.0F, 10.0F));
        partdefinition.addOrReplaceChild("BackLeftBrace", CubeListBuilder.create().texOffs(0, 35).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -19.0F, 10.0F));
        partdefinition.addOrReplaceChild("FrontRightBrace", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(27.0F, -19.0F, -10.0F));
        partdefinition.addOrReplaceChild("BackRightBrace", CubeListBuilder.create().texOffs(0, 35).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -19.0F, -10.0F));

        partdefinition.addOrReplaceChild("TopBrace2", CubeListBuilder.create().texOffs(4, 37).addBox(-1.0F, -1.0F, -14.0F, 2.0F, 2.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, -20.0F, 0.0F, 0.0F, -0.733F, 0.0F));
        partdefinition.addOrReplaceChild("TopBrace1", CubeListBuilder.create().texOffs(4, 37).addBox(-1.0F, -1.0F, -14.0F, 2.0F, 2.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(18.0F, -20.01F, 0.0F, 0.0F, 0.733F, 0.0F));

        PartDefinition hopper1 = partdefinition.addOrReplaceChild("Hopper1", CubeListBuilder.create().texOffs(68, 53).addBox(-7.0F, -7.0F, -14.0F, 13.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -2.0F, 0.5F, 0.0F, -1.5708F, 0.0F));
        PartDefinition hopper2 = hopper1.addOrReplaceChild("Hopper2", CubeListBuilder.create().texOffs(68, 53).addBox(-5.0F, -7.0F, -12.0F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -3.0F, -7.0F, 0.0F, -1.5708F, 0.0F));
        hopper2.addOrReplaceChild("Hopper3", CubeListBuilder.create().texOffs(68, 52).addBox(-2.5F, -7.0F, -9.5F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9F, -1.5F, -7.0F, -1.5708F, -1.5708F, 0.0F));

        partdefinition.addOrReplaceChild("WheelBackLeft", CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 3.0F, 12.0F));
        partdefinition.addOrReplaceChild("WheelFrontLeft", CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(27.0F, 3.0F, 12.0F));
        partdefinition.addOrReplaceChild("WheelBackRight", CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 3.0F, -12.0F));
        partdefinition.addOrReplaceChild("WheelFrontRight", CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(27.0F, 3.0F, -12.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

}