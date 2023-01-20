package baguchan.wasabi.registry;

import baguchan.wasabi.Wasabi;
import baguchan.wasabi.entity.SlashAir;
import baguchan.wasabi.entity.Tengu;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Wasabi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Wasabi.MODID);

	public static final RegistryObject<EntityType<Tengu>> TENGU = ENTITIES_REGISTRY.register("tengu", () -> EntityType.Builder.of(Tengu::new, MobCategory.MONSTER).sized(0.6F, 2.0F).clientTrackingRange(8).build(prefix("tengu")));

	public static final RegistryObject<EntityType<SlashAir>> SLASH_AIR = ENTITIES_REGISTRY.register("slash_air", () -> EntityType.Builder.<SlashAir>of(SlashAir::new, MobCategory.MONSTER).sized(1.0F, 1.0F).clientTrackingRange(8).build(prefix("slash_air")));

	@SubscribeEvent
	public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
		event.put(TENGU.get(), Tengu.createAttributes().build());
	}

	private static String prefix(String path) {
		return Wasabi.MODID + "." + path;
	}
}