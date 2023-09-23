package baguchan.bagusmob.client;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.model.ModifigerModel;
import baguchan.bagusmob.client.model.NinjarModel;
import baguchan.bagusmob.client.model.TenguModel;
import baguchan.bagusmob.client.model.VilerVexModel;
import baguchan.bagusmob.client.render.*;
import baguchan.bagusmob.registry.ModEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BagusMob.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRenderingRegistry {
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityRegistry.TENGU.get(), TenguRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.NINJAR.get(), NinjarRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.SLASH_AIR.get(), SlashAirRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.MODIFIGER.get(), ModifigerRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.VILER_VEX.get(), VilerVexRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.TENGU, TenguModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.NINJAR, NinjarModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.MODIFIGER, ModifigerModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.VILER_VEX, VilerVexModel::createBodyLayer);
	}
}