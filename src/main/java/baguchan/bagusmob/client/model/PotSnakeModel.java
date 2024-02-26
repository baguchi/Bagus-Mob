package baguchan.bagusmob.client.model;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.bagusmob.client.animation.PotSnakeAnimations;
import baguchan.bagusmob.entity.PotSnake;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PotSnakeModel<T extends PotSnake> extends HierarchicalModel<T> {
    private final ModelPart realroot;
    public final ModelPart root;
    public final ModelPart pot;

    public PotSnakeModel(ModelPart root) {
        this.realroot = root;
        this.root = root.getChild("root");
        this.pot = this.root.getChild("pot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.5F, 1.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -4.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 4.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition pot = root.addOrReplaceChild("pot", CubeListBuilder.create(), PartPose.offset(0.0F, -5.5F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity.isHasPot()) {
            if (entity.isHiding()) {
                this.applyStatic(PotSnakeAnimations.HIDING);
            } else {
                this.animate(entity.standingAnimationState, PotSnakeAnimations.STANDING, ageInTicks);
                if (!entity.standingAnimationState.isStarted()) {
                    this.applyStatic(PotSnakeAnimations.STANDING_INIT);
                    this.animateWalk(PotSnakeAnimations.STANDING_WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
                }
            }
        } else {
            this.animateWalk(PotSnakeAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        }
    }

    @Override
    public ModelPart root() {
        return this.realroot;
    }
}