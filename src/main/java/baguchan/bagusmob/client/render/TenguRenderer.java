package baguchan.bagusmob.client.render;

import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.TenguModel;
import baguchan.bagusmob.entity.Tengu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TenguRenderer <T extends Tengu> extends MobRenderer<T, TenguModel<T>> {
	private static final ResourceLocation ILLAGER = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "textures/entity/tengu.png");

	public TenguRenderer(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new TenguModel<>(renderManagerIn.bakeLayer(ModModelLayers.TENGU)), 0.5F);
		this.addLayer(new CustomArmorLayer<>(this, renderManagerIn));
		this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
	}

	protected void setupRotations(T p_114109_, PoseStack p_114110_, float p_114111_, float p_114112_, float p_114113_, float p_319953_) {
		if (p_114109_.isFallFlying()) {
			super.setupRotations(p_114109_, p_114110_, p_114111_, p_114112_, p_114113_, p_319953_);
			float f1 = (float) p_114109_.getFallFlyingTicks() + p_114113_;
			float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			if (!p_114109_.isAutoSpinAttack()) {
				p_114110_.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - p_114109_.getXRot())));
			}

			Vec3 vec3 = p_114109_.getViewVector(p_114113_);
			Vec3 vec31 = p_114109_.getDeltaMovement();
			double d0 = vec31.horizontalDistanceSqr();
			double d1 = vec3.horizontalDistanceSqr();
			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
				double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
				p_114110_.mulPose(Axis.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
			}
		} else {
			super.setupRotations(p_114109_, p_114110_, p_114111_, p_114112_, p_114113_, p_319953_);

		}
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ILLAGER;
	}
}