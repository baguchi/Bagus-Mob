package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BagusMob.MODID)
public class ModCreativeTabs {
	@SubscribeEvent
	public static void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(ModItemRegistry.TENGU_SPAWNEGG.get());
			event.accept(ModItemRegistry.NINJAR_SPAWNEGG.get());
            event.accept(ModItemRegistry.RUDEHOG_SPAWNEGG.get());
            event.accept(ModItemRegistry.HUNTER_BOAR_SPAWNEGG.get());
            event.accept(ModItemRegistry.BURNER_HOG_SPAWNEGG.get());
            event.accept(ModItemRegistry.POT_SNAKE_SPAWNEGG.get());
		}
		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(ModItemRegistry.SHARPED_LEAF.get());
			event.accept(ModItemRegistry.DAGGER.get());
            event.accept(ModItemRegistry.KATANA.get());
			event.accept(ModItemRegistry.NINJA_HOOD.get());
			event.accept(ModItemRegistry.NINJA_CHESTPLATE.get());
			event.accept(ModItemRegistry.NINJA_BOOTS.get());
			event.accept(ModItemRegistry.BEAST_CUDGEL.get());
			event.accept(ModItemRegistry.SPIN_BLADE.get());
		}
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItemRegistry.SNAKE_POTTERY_SHERD.get());
        }
	}
}