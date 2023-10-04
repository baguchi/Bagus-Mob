package baguchan.bagusmob;

import baguchan.bagusmob.registry.ModEffects;
import baguchan.bagusmob.registry.ModEntityRegistry;
import baguchan.bagusmob.registry.ModItemRegistry;
import baguchan.bagusmob.registry.ModSensors;
import baguchan.bagusmob.utils.JigsawHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BagusMob.MODID)
public class BagusMob {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "bagusmob";

	public static boolean greedLoaded;
	public BagusMob() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModEffects.MOB_EFFECTS.register(modEventBus);
		ModItemRegistry.ITEM_REGISTRY.register(modEventBus);
		ModEntityRegistry.ENTITIES_REGISTRY.register(modEventBus);
		ModSensors.SENSOR_TYPES.register(modEventBus);
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.addListener(this::serverStart);
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

    private void commonSetup(final FMLCommonSetupEvent event) {
		greedLoaded = ModList.get().isLoaded("greedandbleed");
		Raid.RaiderType.create("tengu", ModEntityRegistry.TENGU.get(), new int[]{0, 1, 2, 0, 2, 2, 3, 3});
		Raid.RaiderType.create("ninjar", ModEntityRegistry.NINJAR.get(), new int[]{0, 0, 1, 2, 2, 3, 3, 4});
		Raid.RaiderType.create("modifiger", ModEntityRegistry.MODIFIGER.get(), new int[]{0, 0, 0, 0, 1, 1, 2, 3});
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
