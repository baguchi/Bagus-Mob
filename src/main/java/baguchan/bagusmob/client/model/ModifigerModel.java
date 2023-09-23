package baguchan.bagusmob.client.model;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import bagu_chan.bagus_lib.client.layer.IArmor;
import baguchan.bagusmob.client.aniamtion.ModifigerAnimations;
import baguchan.bagusmob.entity.Modifiger;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class ModifigerModel<T extends Modifiger> extends HierarchicalModel<T> implements ArmedModel, HeadedModel, IArmor {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;

    public ModifigerModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_eyeblow = head.addOrReplaceChild("right_eyeblow", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -4.0F, -4.1F));

        PartDefinition left_eyeblow = head.addOrReplaceChild("left_eyeblow", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -2.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -4.0F, -4.1F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cloth = body.addOrReplaceChild("cloth", CubeListBuilder.create().texOffs(16, 39).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 14.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_box = cloth.addOrReplaceChild("right_box", CubeListBuilder.create().texOffs(0, 61).addBox(-5.0F, -1.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.5F)), PartPose.offset(-4.5F, 13.0F, 0.25F));

        PartDefinition left_box = cloth.addOrReplaceChild("left_box", CubeListBuilder.create().texOffs(0, 61).addBox(-1.0F, -1.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.5F)), PartPose.offset(4.5F, 13.0F, 0.25F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);

        this.animateWalk(ModifigerAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        if (!entity.lockSpellAnimationState.isStarted() && !entity.summonSpellAnimationState.isStarted()) {
            this.animateWalk(ModifigerAnimations.WALK_SWING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        }
        this.animate(entity.lockSpellAnimationState, ModifigerAnimations.LOCK_SPELL, ageInTicks);
        this.animate(entity.deathAnimationState, ModifigerAnimations.DEATH, ageInTicks);
        this.animate(entity.summonSpellAnimationState, ModifigerAnimations.SUMMON_SPELL, ageInTicks);

    }

    private ModelPart getArm(HumanoidArm p_102923_) {
        return p_102923_ == HumanoidArm.LEFT ? this.RightArm : this.LeftArm;
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
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of(this.RightArm);
    }

    @Override
    public Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of(this.LeftArm);
    }

    @Override
    public Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of(this.RightLeg);
    }

    @Override
    public Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of(this.LeftLeg);
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
    public ModelPart root() {
        return this.root;
    }
}