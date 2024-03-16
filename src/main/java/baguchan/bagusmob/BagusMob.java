package baguchan.bagusmob;

import baguchan.bagusmob.registry.*;
import baguchan.bagusmob.utils.JigsawHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.raid.Raid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BagusMob.MODID)
public class BagusMob {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "bagusmob";

	public static boolean greedLoaded;

	public BagusMob(IEventBus modEventBus) {

		ModSoundEvents.SOUND_EVENTS.register(modEventBus);
		ModItemRegistry.ITEM_REGISTRY.register(modEventBus);
		ModEntityRegistry.ENTITIES_REGISTRY.register(modEventBus);
		ModSensors.SENSOR_TYPES.register(modEventBus);
        ModPotPatternRegistry.POT_PATTERNS.register(modEventBus);
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.addListener(this::serverStart);
	}

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModPotPatternRegistry.expandVanilla();
		greedLoaded = ModList.get().isLoaded("greedandbleed");
		Raid.RaiderType.create("tengu", ModEntityRegistry.TENGU.get(), new int[]{0, 1, 2, 0, 2, 2, 3, 3});
		Raid.RaiderType.create("ninjar", ModEntityRegistry.NINJAR.get(), new int[]{0, 0, 1, 2, 2, 3, 3, 4});
	}

	private void serverStart(final ServerAboutToStartEvent event) {
		JigsawHelper.registerJigsaw(event.getServer(),
				new ResourceLocation("minecraft:bastion/mobs/piglin_melee"),
				new ResourceLocation(BagusMob.MODID, "bastion/rude_hog"), 1);
		JigsawHelper.registerJigsaw(event.getServer(),
				new ResourceLocation("minecraft:bastion/mobs/piglin"),
				new ResourceLocation(BagusMob.MODID, "bastion/burner_hog"), 1);

	}
}
