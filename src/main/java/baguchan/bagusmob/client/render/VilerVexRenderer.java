package baguchan.bagusmob.client.render;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.VilerVexModel;
import baguchan.bagusmob.entity.VilerVex;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class VilerVexRenderer<T extends VilerVex> extends MobRenderer<T, VilerVexModel<T>> {
    private static final ResourceLocation VEX_LOCATION = new ResourceLocation(BagusMob.MODID, "textures/entity/viler_vex.png");

    public VilerVexRenderer(EntityRendererProvider.Context p_174435_) {
        super(p_174435_, new VilerVexModel<>(p_174435_.bakeLayer(ModModelLayers.VILER_VEX)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this, p_174435_.getItemInHandRenderer()));
    }

    protected int getBlockLightLevel(T p_116298_, BlockPos p_116299_) {
        return 15;
    }

    public ResourceLocation getTextureLocation(T p_116292_) {
        return VEX_LOCATION;
    }
}