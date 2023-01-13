package baguchan.tengu.client;

import baguchan.tengu.TenguCore;
import baguchan.tengu.client.model.TenguModel;
import baguchan.tengu.client.render.TenguRenderer;
import baguchan.tengu.registry.TenguEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TenguCore.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TenguRenderingRegistry {
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(TenguEntityRegistry.TENGU.get(), TenguRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.TENGU, TenguModel::createBodyLayer);
	}
}