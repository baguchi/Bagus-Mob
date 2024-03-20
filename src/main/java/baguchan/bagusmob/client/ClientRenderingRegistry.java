package baguchan.bagusmob.client;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.model.*;
import baguchan.bagusmob.client.render.*;
import baguchan.bagusmob.registry.ModEntityRegistry;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = BagusMob.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRenderingRegistry {
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityRegistry.TENGU.get(), TenguRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.POT_SNAKE.get(), PotSnakeRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.NINJAR.get(), NinjarRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.SAMURAI.get(), SamuraiRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.SLASH_AIR.get(), SlashAirRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.RUDEHOG.get(), RudeHogRenderer::new);
		event.registerEntityRenderer(ModEntityRegistry.HUNTER_BOAR.get(), (p_174165_) -> new HunterBoarRenderer(p_174165_));
        event.registerEntityRenderer(ModEntityRegistry.SPIN_BLADE.get(), SpinBladeRenderer::new);
        event.registerEntityRenderer(ModEntityRegistry.BURNER_HOG.get(), BurnerHogRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.TENGU, TenguModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.POT_SNAKE, PotSnakeModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.NINJAR, NinjarModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.NINJA_ARMOR, NinjaArmorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RUDEHOG, RudeHogModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SPIN_BLADE, SpinBladeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BURNER_HOG, BurnerHogModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SAMURAI, SamuraiModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void modelBake(ModelEvent.ModifyBakingResult event) {
        ModItemRegistry.addItemModelProperties();
	}
}