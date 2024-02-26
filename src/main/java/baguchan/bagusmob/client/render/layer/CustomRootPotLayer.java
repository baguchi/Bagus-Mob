package baguchan.bagusmob.client.render.layer;

import baguchan.bagusmob.client.model.PotSnakeModel;
import baguchan.bagusmob.entity.PotSnake;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;

public class CustomRootPotLayer<T extends PotSnake, M extends PotSnakeModel<T>> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public CustomRootPotLayer(RenderLayerParent<T, M> p_234822_, ItemInHandRenderer p_234831_) {
        super(p_234822_);
        this.itemInHandRenderer = p_234831_;
    }

    public void render(PoseStack p_116639_, MultiBufferSource p_116640_, int p_116641_, T p_116642_, float p_116643_, float p_116644_, float p_116645_, float p_116646_, float p_116647_, float p_116648_) {
        if (p_116642_.isHasPot()) {
            p_116639_.pushPose();

            this.getParentModel().root.translateAndRotate(p_116639_);
            this.getParentModel().pot.translateAndRotate(p_116639_);
            p_116639_.scale(0.65F, -0.65F, -0.65F);
            p_116639_.translate(0F, -15F / 16F, 0F);
            if (!p_116642_.isHiding() && p_116642_.standingAnimationTick >= p_116642_.standingAnimationLength) {
                p_116639_.translate(0F, -2F / 16F, 0F);
            }
            this.itemInHandRenderer.renderItem(p_116642_, p_116642_.getPot(), ItemDisplayContext.HEAD, false, p_116639_, p_116640_, p_116641_);
            p_116639_.popPose();
        }

    }
}