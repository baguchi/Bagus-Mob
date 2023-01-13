package baguchan.tengu.registry;

import baguchan.tengu.TenguCore;
import baguchan.tengu.entity.Tengu;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = TenguCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TenguEntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TenguCore.MODID);

	public static final RegistryObject<EntityType<Tengu>> TENGU = ENTITIES_REGISTRY.register("tengu", () -> EntityType.Builder.of(Tengu::new, MobCategory.MONSTER).sized(0.6F, 2.0F).clientTrackingRange(8).build(prefix("tengu")));
	@SubscribeEvent
	public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
		event.put(TENGU.get(), Tengu.createAttributes().build());
	}

	private static String prefix(String path) {
		return "tengu." + path;
	}
}