package baguchan.wasabi.client.model;// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.wasabi.client.aniamtion.TenguAnimations;
import baguchan.wasabi.entity.Tengu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;

public class TenguModel<T extends Tengu> extends HierarchicalModel<T> implements ArmedModel, HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

	private final ModelPart roots;
	private final ModelPart head;
	private final ModelPart nose;
	private final ModelPart body;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart cape_right;
	private final ModelPart cape_left;

	public TenguModel(ModelPart root) {
		this.roots = root;
		this.head = root.getChild("head");
		this.nose = this.head.getChild("nose");
		this.body = root.getChild("body");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.cape_right = root.getChild("cape_right");
		this.cape_left = root.getChild("cape_left");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-2.0F, 12.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 10.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-1.0F, 12.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 10.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition cape_right = partdefinition.addOrReplaceChild("cape_right", CubeListBuilder.create().texOffs(65, 0).addBox(-8.0F, 0.0F, -1.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cape_left = partdefinition.addOrReplaceChild("cape_left", CubeListBuilder.create().texOffs(65, 0).addBox(0.0F, 0.0F, -1.0F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.3491F, 0.0F, 0.0F));

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

		if (this.riding) {
			this.right_arm.xRot = (-(float) Math.PI / 5F);
			this.right_arm.yRot = 0.0F;
			this.right_arm.zRot = 0.0F;
			this.left_arm.xRot = (-(float) Math.PI / 5F);
			this.left_arm.yRot = 0.0F;
			this.left_arm.zRot = 0.0F;
			this.right_leg.xRot = -1.4137167F;
			this.right_leg.yRot = ((float) Math.PI / 10F);
			this.right_leg.zRot = 0.07853982F;
			this.left_leg.xRot = -1.4137167F;
			this.left_leg.yRot = (-(float) Math.PI / 10F);
			this.left_leg.zRot = -0.07853982F;
		} else {
			this.right_arm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.right_arm.yRot = 0.0F;
			this.right_arm.zRot = 0.0F;
			this.left_arm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.left_arm.yRot = 0.0F;
			this.left_arm.zRot = 0.0F;
			this.right_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.right_leg.yRot = 0.0F;
			this.right_leg.zRot = 0.0F;
			this.left_leg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.left_leg.yRot = 0.0F;
			this.left_leg.zRot = 0.0F;
		}

		AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = entityIn.getArmPose();
		if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CELEBRATING) {
			this.right_arm.z = 0.0F;
			this.right_arm.x = -5.0F;
			this.right_arm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
			this.right_arm.zRot = 2.670354F;
			this.right_arm.yRot = 0.0F;
			this.left_arm.z = 0.0F;
			this.left_arm.x = 5.0F;
			this.left_arm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
			this.left_arm.zRot = -2.3561945F;
			this.left_arm.yRot = 0.0F;
		}
		this.cape_right.xRot = 0.3491F + 0.17453294F * limbSwingAmount * 1.75F;
		this.cape_left.xRot = 0.3491F + 0.17453294F * limbSwingAmount * 1.75F;

		this.cape_right.zRot = -f1;
		this.cape_left.zRot = f1;
		this.animate(entityIn.slashRightAnimationState, TenguAnimations.SLASH_RIGHT, ageInTicks);
		this.animate(entityIn.slashLeftAnimationState, TenguAnimations.SLASH_LEFT, ageInTicks);
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
}