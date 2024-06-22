package baguchan.bagusmob;

import baguchan.bagusmob.registry.*;
import baguchan.bagusmob.utils.JigsawHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BagusMob.MODID)
public class BagusMob {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "bagusmob";

	public static boolean greedLoaded;

	public BagusMob(IEventBus modEventBus) {

		ModSoundEvents.SOUND_EVENTS.register(modEventBus);
		ModItemRegistry.ITEM_REGISTRY.register(modEventBus);
		ModEntityRegistry.ENTITIES_REGISTRY.register(modEventBus);
		ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
		ModSensors.SENSOR_TYPES.register(modEventBus);
        ModPotPatternRegistry.POT_PATTERNS.register(modEventBus);
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.addListener(this::serverStart);
	}

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModPotPatternRegistry.expandVanilla();
		greedLoaded = ModList.get().isLoaded("greedandbleed");
	}

	private void serverStart(final ServerAboutToStartEvent event) {
		JigsawHelper.registerJigsaw(event.getServer(),
				ResourceLocation.parse("minecraft:bastion/mobs/piglin_melee"),
				ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "bastion/rude_hog"), 1);
		JigsawHelper.registerJigsaw(event.getServer(),
				ResourceLocation.parse("minecraft:trial_chambers/decor"),
				ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "trial_chambers/decor/snake_guster_pot"), 1);
		JigsawHelper.registerJigsaw(event.getServer(),
				ResourceLocation.parse("minecraft:trial_chambers/decor"),
				ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "trial_chambers/decor/snake_flow_pot"), 1);

	}
}
