package baguchan.bagusmob.client.render;

import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.BurnerHogModel;
import baguchan.bagusmob.entity.BurnerHog;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BurnerHogRenderer<T extends BurnerHog> extends MobRenderer<T, BurnerHogModel<T>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BagusMob.MODID, "textures/entity/piglin/burner_hog.png");

    public BurnerHogRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BurnerHogModel<>(renderManagerIn.bakeLayer(ModModelLayers.BURNER_HOG)), 0.5F);
        this.addLayer(new CustomArmorLayer<>(this, renderManagerIn));
    }

    protected boolean isShaking(T p_114864_) {
        return super.isShaking(p_114864_) || p_114864_.isConverting();
    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return TEXTURE;
    }
}
