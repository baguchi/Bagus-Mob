package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BagusMob.MODID)
public class ModCreativeTabs {
	@SubscribeEvent
	public static void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(ModItemRegistry.TENGU_SPAWNEGG.get());
			event.accept(ModItemRegistry.NINJAR_SPAWNEGG.get());
			event.accept(ModItemRegistry.MODIFIGER_SPAWNEGG.get());
			event.accept(ModItemRegistry.VILER_VEX_SPAWNEGG.get());
		}
		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(ModItemRegistry.SHARPED_LEAF.get());
			event.accept(ModItemRegistry.DAGGER.get());
		}
	}
}