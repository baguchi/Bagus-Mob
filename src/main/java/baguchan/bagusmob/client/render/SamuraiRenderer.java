package baguchan.bagusmob.client.render;

import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.SamuraiModel;
import baguchan.bagusmob.entity.Samurai;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SamuraiRenderer<T extends Samurai> extends MobRenderer<T, SamuraiModel<T>> {
    private static final ResourceLocation ILLAGER = new ResourceLocation(BagusMob.MODID, "textures/entity/samurai.png");

    public SamuraiRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SamuraiModel<>(renderManagerIn.bakeLayer(ModModelLayers.SAMURAI)), 0.5F);
        this.addLayer(new CustomArmorLayer<>(this, renderManagerIn));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return ILLAGER;
    }
}