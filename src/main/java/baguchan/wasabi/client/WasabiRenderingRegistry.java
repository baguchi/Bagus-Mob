package baguchan.wasabi.client;

import baguchan.wasabi.Wasabi;
import baguchan.wasabi.client.model.NinjarModel;
import baguchan.wasabi.client.model.TenguModel;
import baguchan.wasabi.client.render.NinjarRenderer;
import baguchan.wasabi.client.render.SlashAirRenderer;
import baguchan.wasabi.client.render.TenguRenderer;
import baguchan.wasabi.registry.ModEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Wasabi.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WasabiRenderingRegistry {
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityRegistry.TENGU.get(), TenguRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.NINJAR.get(), NinjarRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.SLASH_AIR.get(), SlashAirRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.TENGU, TenguModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.NINJAR, NinjarModel::createBodyLayer);
	}
}