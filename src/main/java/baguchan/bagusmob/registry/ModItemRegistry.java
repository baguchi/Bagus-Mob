package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.item.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BagusMob.MODID);
	public static final RegistryObject<Item> TENGU_SPAWNEGG = ITEM_REGISTRY.register("tengu_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.TENGU, 0x959B9B, 0xDFD96D, (new Item.Properties())));
	public static final RegistryObject<Item> NINJAR_SPAWNEGG = ITEM_REGISTRY.register("ninjar_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.NINJAR, 0x323237, 0x959B9B, (new Item.Properties())));
	public static final RegistryObject<Item> MODIFIGER_SPAWNEGG = ITEM_REGISTRY.register("modifiger_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.MODIFIGER, 0x959B9B, 0x7341BC, (new Item.Properties())));
    public static final RegistryObject<Item> VILER_VEX_SPAWNEGG = ITEM_REGISTRY.register("viler_vex_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.VILER_VEX, 0x7341BC, 0x8961AA, (new Item.Properties())));

	public static final RegistryObject<Item> SHARPED_LEAF = ITEM_REGISTRY.register("sharped_leaf", () -> new ShapedLeafItem((new Item.Properties().durability(128))));
	public static final RegistryObject<Item> DAGGER = ITEM_REGISTRY.register("dagger", () -> new DaggerItem((new Item.Properties().durability(242))));

	public static final RegistryObject<Item> NINJA_BOOTS = ITEM_REGISTRY.register("ninja_boots", () -> new NinjaArmorItem(ArmorItem.Type.BOOTS, (new Item.Properties())));
	public static final RegistryObject<Item> NINJA_CHESTPLATE = ITEM_REGISTRY.register("ninja_chestplate", () -> new NinjaArmorItem(ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
	public static final RegistryObject<Item> NINJA_HOOD = ITEM_REGISTRY.register("ninja_hood", () -> new NinjaArmorItem(ArmorItem.Type.HELMET, (new Item.Properties())));
    public static final RegistryObject<Item> RUDEHOG_SPAWNEGG = ITEM_REGISTRY.register("rudehog_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.RUDEHOG, 0x85424C, 0x361D12, (new Item.Properties())));
    public static final RegistryObject<Item> HUNTER_BOAR_SPAWNEGG = ITEM_REGISTRY.register("hunter_boar_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.HUNTER_BOAR, 0x85424C, 0xFCED6B, (new Item.Properties())));
    public static final RegistryObject<Item> BURNER_HOG_SPAWNEGG = ITEM_REGISTRY.register("burner_hog_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.BURNER_HOG, 0xF2BA86, 0x545054, (new Item.Properties())));

	public static final RegistryObject<Item> BEAST_CUDGEL = ITEM_REGISTRY.register("beast_cudgel", () -> new BeastWeaponItem((new Item.Properties()).durability(520).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SPIN_BLADE = ITEM_REGISTRY.register("spin_blade", () -> new SpinBladeItem((new Item.Properties()).durability(320).rarity(Rarity.UNCOMMON)));

	@OnlyIn(Dist.CLIENT)
	public static void addItemModelProperties() {

		ItemProperties.register(SPIN_BLADE.get(), new ResourceLocation(BagusMob.MODID, "thrown"), (stack, world, entity, idk) ->
				SpinBladeItem.getThrownUuid(stack) != null ? 1 : 0);
	}
}