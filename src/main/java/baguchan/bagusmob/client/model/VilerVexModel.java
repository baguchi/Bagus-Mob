package baguchan.bagusmob.client.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.bagusmob.client.aniamtion.VilerVexAnimations;
import baguchan.bagusmob.entity.VilerVex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;

public class VilerVexModel<T extends VilerVex> extends HierarchicalModel<T> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart left_wing;
    private final ModelPart right_wing;

    public VilerVexModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.left_wing = root.getChild("left_wing");
        this.right_wing = root.getChild("right_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.5F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.5F, 0.0F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 19.0F, 1.0F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 19.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 32, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.left_wing.yRot = 1.0995574F + Mth.cos(ageInTicks * 45.836624F * ((float) Math.PI / 180F)) * ((float) Math.PI / 180F) * 16.2F;
        this.right_wing.yRot = -this.left_wing.yRot;
        this.left_wing.xRot = 0.47123888F;
        this.left_wing.zRot = -0.47123888F;
        this.right_wing.xRot = 0.47123888F;
        this.right_wing.zRot = 0.47123888F;
        if (!entity.isChargingCrossbow()) {
            if (entity.isHolding(Items.CROSSBOW)) {
                AnimationUtils.animateCrossbowHold(this.right_arm, this.left_arm, this.head, true);
            } else {
                this.animateWalk(VilerVexAnimations.IDLE, ageInTicks, 1.0F, 1.0F, 1.0F);
            }
        } else {
            AnimationUtils.animateCrossbowCharge(this.right_arm, this.left_arm, entity, true);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void translateToHand(HumanoidArm p_259770_, PoseStack p_260351_) {
        boolean flag = p_259770_ == HumanoidArm.RIGHT;
        ModelPart modelpart = flag ? this.right_arm : this.left_arm;
        modelpart.translateAndRotate(p_260351_);
        p_260351_.scale(0.55F, 0.55F, 0.55F);
        this.offsetStackPosition(p_260351_, flag);
    }

    private void offsetStackPosition(PoseStack p_263343_, boolean p_263414_) {
    }
}