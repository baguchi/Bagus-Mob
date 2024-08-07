package baguchan.bagusmob.client.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.bagusmob.client.animation.SoilthAnimations;
import baguchan.bagusmob.entity.Soilth;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SoilthModel<T extends Soilth> extends HierarchicalModel<T> {
    private final ModelPart roots;
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart soils;
    private final ModelPart soil_top;
    private final ModelPart soil_middle;
    private final ModelPart soil_bottom;

    public SoilthModel(ModelPart root) {
        this.roots = root;
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.soils = this.root.getChild("soils");
        this.soil_top = this.soils.getChild("soil_top");
        this.soil_middle = this.soils.getChild("soil_middle");
        this.soil_bottom = this.soils.getChild("soil_bottom");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition soils = root.addOrReplaceChild("soils", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition soil_top = soils.addOrReplaceChild("soil_top", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -4.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 18).addBox(0.0F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 5.0F, -1.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition soil_middle = soils.addOrReplaceChild("soil_middle", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-3.0F, -4.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 18).mirror().addBox(-3.0F, -1.0F, -1.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 11.0F, 1.0F, 0.0F, 0.6109F, 0.0F));

        PartDefinition soil_bottom = soils.addOrReplaceChild("soil_bottom", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 18).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 1.0F, 0.0F, -0.6981F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Soilth entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);

        this.animate(entity.chargeAnimationState, SoilthAnimations.charge, ageInTicks);
        this.animate(entity.deadAnimationState, SoilthAnimations.dead, ageInTicks);
        this.animate(entity.idleAnimationState, SoilthAnimations.idle, ageInTicks);
        this.animate(entity.shootAnimationState, SoilthAnimations.shoot, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.roots;
    }
}