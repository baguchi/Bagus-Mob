package baguchan.bagusmob.client.render;

import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.ModifigerModel;
import baguchan.bagusmob.entity.Modifiger;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModifigerRenderer<T extends Modifiger> extends MobRenderer<T, ModifigerModel<T>> {
    private static final ResourceLocation ILLAGER = new ResourceLocation(BagusMob.MODID, "textures/entity/modifiger.png");

    public ModifigerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModifigerModel<>(renderManagerIn.bakeLayer(ModModelLayers.MODIFIGER)), 0.5F);
        this.addLayer(new CustomArmorLayer<>(this, renderManagerIn));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    protected float getFlipDegrees(T p_115337_) {
        return 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return ILLAGER;
    }
}