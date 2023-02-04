package baguchan.wasabi.registry;

import baguchan.wasabi.Wasabi;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Wasabi.MODID)
public class ModCreativeTabs {
	@SubscribeEvent
	public static void addCreativeTab(CreativeModeTabEvent.BuildContents event){
		if (event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(ModItemRegistry.TENGU_SPAWNEGG.get());
			event.accept(ModItemRegistry.NINJAR_SPAWNEGG.get());
		}
		if (event.getTab() == CreativeModeTabs.COMBAT) {
			event.accept(ModItemRegistry.SHARPED_LEAF.get());
			event.accept(ModItemRegistry.DAGGER.get());
		}
	}
}