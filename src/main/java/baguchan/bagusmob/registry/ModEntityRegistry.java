package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BagusMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BagusMob.MODID);

	public static final RegistryObject<EntityType<Tengu>> TENGU = ENTITIES_REGISTRY.register("tengu", () -> EntityType.Builder.of(Tengu::new, MobCategory.MONSTER).sized(0.6F, 2.0F).clientTrackingRange(8).build(prefix("tengu")));
	public static final RegistryObject<EntityType<Ninjar>> NINJAR = ENTITIES_REGISTRY.register("ninjar", () -> EntityType.Builder.of(Ninjar::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(prefix("ninjar")));
	public static final RegistryObject<EntityType<Modifiger>> MODIFIGER = ENTITIES_REGISTRY.register("modifiger", () -> EntityType.Builder.of(Modifiger::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(prefix("modifiger")));
	public static final RegistryObject<EntityType<VilerVex>> VILER_VEX = ENTITIES_REGISTRY.register("viler_vex", () -> EntityType.Builder.of(VilerVex::new, MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8).build(prefix("viler_vex")));


	public static final RegistryObject<EntityType<SlashAir>> SLASH_AIR = ENTITIES_REGISTRY.register("slash_air", () -> EntityType.Builder.<SlashAir>of(SlashAir::new, MobCategory.MONSTER).sized(1.0F, 1.0F).clientTrackingRange(8).build(prefix("slash_air")));

	@SubscribeEvent
	public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
		event.put(TENGU.get(), Tengu.createAttributes().build());
		event.put(NINJAR.get(), Ninjar.createAttributes().build());
		event.put(MODIFIGER.get(), Modifiger.createAttributes().build());
		event.put(VILER_VEX.get(), VilerVex.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerEntityAttribute(SpawnPlacementRegisterEvent event) {
		event.register(TENGU.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(NINJAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(MODIFIGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(VILER_VEX.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

	}

	private static String prefix(String path) {
		return BagusMob.MODID + "." + path;
	}
}