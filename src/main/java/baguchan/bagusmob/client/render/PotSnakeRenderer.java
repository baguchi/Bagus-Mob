package baguchan.bagusmob.client.render;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.PotSnakeModel;
import baguchan.bagusmob.client.render.layer.CustomRootPotLayer;
import baguchan.bagusmob.entity.PotSnake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PotSnakeRenderer<T extends PotSnake> extends MobRenderer<T, PotSnakeModel<T>> {
    private static final ResourceLocation SNAKE = new ResourceLocation(BagusMob.MODID, "textures/entity/pot_snake.png");

    public PotSnakeRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new PotSnakeModel<>(renderManagerIn.bakeLayer(ModModelLayers.POT_SNAKE)), 0.3F);
        this.addLayer(new CustomRootPotLayer<>(this, renderManagerIn.getItemInHandRenderer()));

    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return SNAKE;
    }
}