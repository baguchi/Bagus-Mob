package baguchan.bagusmob.client.render;

import baguchan.bagusmob.entity.projectile.SlashAir;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlashAirRenderer<T extends SlashAir> extends EntityRenderer<T> {

	public SlashAirRenderer(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public void render(T p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return null;
	}
}