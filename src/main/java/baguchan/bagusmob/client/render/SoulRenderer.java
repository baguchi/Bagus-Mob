package baguchan.bagusmob.client.render;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.SoulModel;
import baguchan.bagusmob.entity.projectile.SoulProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulRenderer extends EntityRenderer<SoulProjectile> {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "textures/entity/soilth/soilth.png");
    private final SoulModel<SoulProjectile> model;

    public SoulRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.model = new SoulModel<>(renderManager.bakeLayer(ModModelLayers.SOUL));
    }

    @Override
    public void render(SoulProjectile entityIn, float entityYaw, float partialTicks, PoseStack stackIn, MultiBufferSource bufferIn, int packedLightIn) {
        stackIn.pushPose();
        stackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 180F));
        stackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));

        stackIn.translate(0.0F, -1.501F, 0.0F);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(this.model.renderType(LOCATION));
        this.model.renderToBuffer(stackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(0.5F, 1F, 1F, 1F));

        stackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, stackIn, bufferIn, packedLightIn);
    }

    protected int getBlockLightLevel(SoulProjectile p_113910_, BlockPos p_113911_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(SoulProjectile p_115034_) {
        return LOCATION;
    }
}
