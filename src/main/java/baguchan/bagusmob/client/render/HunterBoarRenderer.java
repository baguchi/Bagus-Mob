package baguchan.bagusmob.client.render;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.entity.HunterBoar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HoglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HunterBoarRenderer<T extends HunterBoar> extends HoglinRenderer {
    private static final ResourceLocation HOGLIN_LOCATION = new ResourceLocation(BagusMob.MODID, "textures/entity/hoglin.png");

    public HunterBoarRenderer(EntityRendererProvider.Context p_174165_) {
        super(p_174165_);
    }

    public ResourceLocation getTextureLocation(Hoglin p_114862_) {
        return HOGLIN_LOCATION;
    }

    protected boolean isShaking(Hoglin p_114864_) {
        return super.isShaking(p_114864_) || p_114864_.isConverting();
    }
}