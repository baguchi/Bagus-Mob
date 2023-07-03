package baguchan.bagusmob.client.render;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.NinjarModel;
import baguchan.bagusmob.entity.Ninjar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NinjarRenderer<T extends Ninjar> extends MobRenderer<T, NinjarModel<T>> {
	private static final ResourceLocation ILLAGER = new ResourceLocation(BagusMob.MODID, "textures/entity/ninjar.png");

	public NinjarRenderer(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new NinjarModel<>(renderManagerIn.bakeLayer(ModModelLayers.NINJAR)), 0.5F);
		this.addLayer(new CustomHeadLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
		this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(T p_110775_1_) {
		return ILLAGER;
	}
}