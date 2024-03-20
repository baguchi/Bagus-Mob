package baguchan.bagusmob.client.model;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import bagu_chan.bagus_lib.client.layer.IArmor;
import baguchan.bagusmob.client.animation.HumanoidAnimations;
import baguchan.bagusmob.client.animation.IllagerAnimations;
import baguchan.bagusmob.client.animation.NinjarAnimations;
import baguchan.bagusmob.client.animation.SamuraiAnimations;
import baguchan.bagusmob.entity.Samurai;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class SamuraiModel<T extends Samurai> extends HierarchicalModel<T> implements ArmedModel, HeadedModel, IArmor {
    private final ModelPart root;
    private final ModelPart roots;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public SamuraiModel(ModelPart root) {
        this.root = root;
        this.roots = root.getChild("roots");
        this.head = this.roots.getChild("head");
        this.body = this.roots.getChild("body");
        this.right_arm = this.roots.getChild("right_arm");
        this.left_arm = this.roots.getChild("left_arm");
        this.right_leg = this.roots.getChild("right_leg");
        this.left_leg = this.roots.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition roots = partdefinition.addOrReplaceChild("roots", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = roots.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -9.5F, 0.0F));

        PartDefinition bone = hat.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(76, 0).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(50, 0).addBox(-5.0F, -2.0F, -4.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.48F));

        PartDefinition bone6 = bone2.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(100, 0).addBox(-5.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 0.2618F, -0.5236F, 0.0F));

        PartDefinition bone7 = bone2.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(100, 0).addBox(-5.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5F, -0.2618F, 0.5236F, 0.0F));

        PartDefinition bone8 = bone2.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(100, 0).addBox(-5.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.75F, 3.0F, 3.5F, 0.2182F, 2.3562F, 0.829F));

        PartDefinition bone9 = bone2.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(100, 0).addBox(-5.0F, -2.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.75F, 3.0F, -3.0F, -0.2618F, -2.3562F, 0.9163F));

        PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(50, 0).addBox(0.0F, -2.0F, -4.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.48F));

        PartDefinition bone4 = bone.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition bone5 = bone.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition left_leg = roots.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.0F, 0.0F));

        PartDefinition right_leg = roots.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -12.0F, 0.0F));

        PartDefinition right_arm = roots.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -22.0F, 0.0F));

        PartDefinition left_arm = roots.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -22.0F, 0.0F));

        PartDefinition body = roots.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);

        this.animateWalk(NinjarAnimations.IDLE, ageInTicks, 1, 0.5F, 1);
        float f = ageInTicks - (float) entity.tickCount;

        boolean flag = false;

        if (entity.isBlocking() && !entity.attackAnimationState.isStarted()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                this.applyStatic(SamuraiAnimations.guard_right);
            } else {
                this.applyStatic(SamuraiAnimations.guard_left);
            }
            flag = true;
        } else if (entity.isAggressive() && !entity.attackAnimationState.isStarted()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                this.applyStatic(SamuraiAnimations.pre_attack_right);
            } else {
                this.applyStatic(SamuraiAnimations.pre_attack_left);
            }
            flag = true;
        }


        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            this.animate(entity.attackAnimationState, SamuraiAnimations.attack_right, ageInTicks);
        } else {
            this.animate(entity.attackAnimationState, SamuraiAnimations.attack_left, ageInTicks);
        }

        if (riding) {
            this.applyStatic(HumanoidAnimations.SIT);
        } else {
            this.animateWalk(HumanoidAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
            if (!flag) {
                this.animateWalk(HumanoidAnimations.WALK_SWING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
            }
        }

        if (entity.isCelebrating()) {
            this.animateWalk(IllagerAnimations.CEREBRATE, ageInTicks, 1, 1, 1);

        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }


    private ModelPart getArm(HumanoidArm p_102923_) {
        return p_102923_ == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }

    public void translateToHand(HumanoidArm p_102925_, PoseStack p_102926_) {
        this.roots.translateAndRotate(p_102926_);
        this.getArm(p_102925_).translateAndRotate(p_102926_);
    }

    @Override
    public void translateToHead(ModelPart modelPart, PoseStack poseStack) {
        this.roots.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
        this.roots.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
        poseStack.translate(0, -(24F / 16F), 0);
    }

    @Override
    public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
        this.roots.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {
        this.roots.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of(this.right_arm);
    }

    @Override
    public Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of(this.left_arm);
    }

    @Override
    public Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of(this.right_leg);
    }

    @Override
    public Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of(this.left_leg);
    }

    @Override
    public Iterable<ModelPart> bodyPartArmors() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }
}