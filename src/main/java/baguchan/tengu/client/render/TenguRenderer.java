package baguchan.tengu.client.render;

import baguchan.tengu.TenguCore;
import baguchan.tengu.client.ModModelLayers;
import baguchan.tengu.client.model.TenguModel;
import baguchan.tengu.entity.Tengu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TenguRenderer <T extends Tengu> extends MobRenderer<T, TenguModel<T>> {
	private static final ResourceLocation ILLAGER = new ResourceLocation(TenguCore.MODID, "textures/entity/tengu.png");

	public TenguRenderer(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new TenguModel<>(renderManagerIn.bakeLayer(ModModelLayers.TENGU)), 0.5F);
		this.addLayer(new CustomHeadLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
		this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
	}

	protected void setupRotations(T p_117802_, PoseStack p_117803_, float p_117804_, float p_117805_, float p_117806_) {
		float f = p_117802_.getSwimAmount(p_117806_);
		if (p_117802_.isFallFlying()) {
			super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
			float f1 = (float)p_117802_.getFallFlyingTicks() + p_117806_;
			float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			if (!p_117802_.isAutoSpinAttack()) {
				p_117803_.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - p_117802_.getXRot())));
			}

			Vec3 vec3 = p_117802_.getViewVector(p_117806_);
			Vec3 vec31 = p_117802_.getDeltaMovement();
			double d0 = vec31.horizontalDistanceSqr();
			double d1 = vec3.horizontalDistanceSqr();
			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
				double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
				p_117803_.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
			}
		} else {
			super.setupRotations(p_117802_, p_117803_, p_117804_, p_117805_, p_117806_);
		}

	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ILLAGER;
	}
}