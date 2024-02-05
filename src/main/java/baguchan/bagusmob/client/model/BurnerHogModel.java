package baguchan.bagusmob.client.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import bagu_chan.bagus_lib.client.layer.IArmor;
import baguchan.bagusmob.client.animation.BurnerHogAnimations;
import baguchan.bagusmob.client.animation.HumanoidAnimations;
import baguchan.bagusmob.entity.BurnerHog;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BurnerHogModel<T extends BurnerHog> extends HierarchicalModel<T> implements IArmor {
    private final ModelPart root;
    private final ModelPart bone;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public BurnerHogModel(ModelPart root) {
        this.root = root;
        this.bone = root.getChild("bone");
        this.body = this.bone.getChild("body");
        this.head = this.body.getChild("head");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_leg = this.bone.getChild("right_leg");
        this.left_leg = this.bone.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(36, 28).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 44).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition backpack = body.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -20.0F, 0.0F, 13.0F, 20.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(108, 118).addBox(-5.0F, -11.0F, 7.0F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 2.0F));

        PartDefinition door = backpack.addOrReplaceChild("door", CubeListBuilder.create().texOffs(48, 51).addBox(5.0F, -26.5F, -29.0F, 11.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.5F, 17.5F, 37.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 28).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(60, 38).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 63).addBox(2.0F, -2.0F, -6.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 57).addBox(0.0F, -2.0F, -7.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 31).addBox(-2.0F, -2.0F, -7.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(42, 0).addBox(-1.0F, -10.0F, -4.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(20, 60).addBox(-1.0F, -10.0F, -7.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -8.0F, 4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(10, 60).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 60).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 44).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(58, 12).addBox(0.5F, -2.5F, -2.5F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -10.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(44, 57).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(56, 23).addBox(-3.5F, 3.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(34, 0).addBox(-3.0F, 5.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

        PartDefinition cannon = right_arm.addOrReplaceChild("cannon", CubeListBuilder.create().texOffs(56, 0).addBox(-31.5F, 30.0F, 27.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 60).addBox(-30.5F, 31.0F, 28.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(28.0F, -23.0F, -30.0F));

        PartDefinition finger = cannon.addOrReplaceChild("finger", CubeListBuilder.create().texOffs(0, 28).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.0F, 33.0F, 28.0F));

        PartDefinition finger3 = cannon.addOrReplaceChild("finger3", CubeListBuilder.create().texOffs(4, 28).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-27.0F, 33.0F, 30.0F));

        PartDefinition finger2 = cannon.addOrReplaceChild("finger2", CubeListBuilder.create().texOffs(28, 28).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.0F, 33.0F, 32.0F));

        PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

        PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(42, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        float f = ageInTicks - entity.tickCount;

        if (entity.isCharge()) {
            this.applyStatic(BurnerHogAnimations.BURNING);
        } else {
            this.animateWalk(HumanoidAnimations.WALK_SWING, limbSwing, entity.movingScale.getAnimationScale(f) * limbSwingAmount, 2.0F, 2.5F);
        }
        this.animateWalk(BurnerHogAnimations.IDLE, ageInTicks, (1.0F - entity.movingScale.getAnimationScale(f)), 1.0F, 1.0F);
        this.animateWalk(BurnerHogAnimations.WALK, limbSwing, entity.movingScale.getAnimationScale(f) * limbSwingAmount, 2.0F, 2.5F);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void translateToHead(ModelPart modelPart, PoseStack poseStack) {
        this.bone.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
        this.bone.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
        this.bone.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {
        this.bone.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(1.05F, 1.05F, 1.05F);
    }

    public Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of(this.right_arm);
    }

    public Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of(this.left_arm);
    }

    public Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of(this.right_leg);
    }

    public Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of(this.left_leg);
    }

    public Iterable<ModelPart> bodyPartArmors() {
        return ImmutableList.of(this.body);
    }

    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }
}