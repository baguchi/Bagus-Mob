package baguchan.bagusmob.client.model;// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import bagu_chan.bagus_lib.client.layer.IArmor;
import baguchan.bagusmob.client.aniamtion.IllagerAnimations;
import baguchan.bagusmob.client.aniamtion.TenguAnimations;
import baguchan.bagusmob.entity.Tengu;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class TenguModel<T extends Tengu> extends HierarchicalModel<T> implements ArmedModel, HeadedModel, IArmor {
	private final ModelPart roots;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart cape_right;
	private final ModelPart cape_left;
	private final ModelPart right_arm;
	private final ModelPart left_arm;

	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public TenguModel(ModelPart root) {
		this.roots = root;
		this.body = root.getChild("body");
		this.right_arm = body.getChild("right_arm");
		this.left_arm = body.getChild("left_arm");
		this.right_leg = body.getChild("right_leg");
		this.left_leg = body.getChild("left_leg");
		this.head = body.getChild("head");
		this.cape_right = body.getChild("cape_right");
		this.cape_left = body.getChild("cape_left");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-2.0F, 12.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-1.0F, 12.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition cape_right = body.addOrReplaceChild("cape_right", CubeListBuilder.create().texOffs(65, 0).addBox(-8.0F, 0.0F, -1.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 3.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cape_left = body.addOrReplaceChild("cape_left", CubeListBuilder.create().texOffs(65, 0).addBox(0.0F, 0.0F, -1.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 3.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

		boolean flag = entityIn.getFallFlyingTicks() > 4;

		float f = 0.2617994F;
		float f1 = -0.3491F;
		if (entityIn.isFallFlying()) {
			f1 -= Mth.cos(limbSwing * 0.8F + (float) Math.PI) * 0.8F * limbSwingAmount * 0.5F;
		}

		if (flag) {
			this.head.xRot = (-(float) Math.PI / 4F);
		} else {
			this.head.xRot = headPitch * ((float) Math.PI / 180F);
		}

		this.cape_right.xRot = 0.3491F + 0.17453294F * limbSwingAmount * 1.75F;
		this.cape_left.xRot = 0.3491F + 0.17453294F * limbSwingAmount * 1.75F;

		this.cape_right.zRot = -f1;
		this.cape_left.zRot = f1;
		this.animate(entityIn.slashRightAnimationState, TenguAnimations.SLASH_RIGHT, ageInTicks);
		this.animate(entityIn.slashLeftAnimationState, TenguAnimations.SLASH_LEFT, ageInTicks);
		this.animate(entityIn.fallAnimationState, TenguAnimations.STUN, ageInTicks);
		this.animateWalk(TenguAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        if (entityIn.isCelebrating()) {
            this.animateWalk(IllagerAnimations.CEREBRATE, ageInTicks, 1, 1, 1);

        }
	}

	@Override
	public ModelPart root() {
		return this.roots;
	}

	private ModelPart getArm(HumanoidArm p_102923_) {
		return p_102923_ == HumanoidArm.LEFT ? this.right_arm : this.left_arm;
	}

	public void translateToHand(HumanoidArm p_102925_, PoseStack p_102926_) {
		this.getArm(p_102925_).translateAndRotate(p_102926_);
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}

	@Override
	public void translateToHead(ModelPart modelPart, PoseStack poseStack) {
		this.body.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
		this.body.translateAndRotate(poseStack);
		modelPart.translateAndRotate(poseStack);
	}

	@Override
	public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {
		this.body.translateAndRotate(poseStack);
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
}