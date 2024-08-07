package baguchan.bagusmob.client.render;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.SoilthModel;
import baguchan.bagusmob.entity.Soilth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class SoilthRenderer<T extends Soilth> extends MobRenderer<T, SoilthModel<T>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "textures/entity/soilth/soilth.png");
    private static final ResourceLocation DEAD_TEXTURE = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "textures/entity/soilth/soilth_soil.png");


    public SoilthRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SoilthModel<>(renderManagerIn.bakeLayer(ModModelLayers.SOILTH)), 0.5F);
    }

    protected int getBlockLightLevel(T p_113910_, BlockPos p_113911_) {
        return p_113910_.isAlive() ? 15 : super.getBlockLightLevel(p_113910_, p_113911_);
    }

    protected float getFlipDegrees(T p_115337_) {
        return 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isDeadOrDying()) {
            return DEAD_TEXTURE;
        }
        return TEXTURE;
    }
}
