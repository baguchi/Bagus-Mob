package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.item.DaggerItem;
import baguchan.bagusmob.item.ShapedLeafItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BagusMob.MODID);
	public static final RegistryObject<Item> TENGU_SPAWNEGG = ITEM_REGISTRY.register("tengu_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.TENGU, 0x959B9B, 0xDFD96D, (new Item.Properties())));
	public static final RegistryObject<Item> NINJAR_SPAWNEGG = ITEM_REGISTRY.register("ninjar_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.NINJAR, 0x323237, 0x959B9B, (new Item.Properties())));
	public static final RegistryObject<Item> MODIFIGER_SPAWNEGG = ITEM_REGISTRY.register("modifiger_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.MODIFIGER, 0x959B9B, 0x7341BC, (new Item.Properties())));

	public static final RegistryObject<Item> SHARPED_LEAF = ITEM_REGISTRY.register("sharped_leaf", () -> new ShapedLeafItem((new Item.Properties().durability(128))));
	public static final RegistryObject<Item> DAGGER = ITEM_REGISTRY.register("dagger", () -> new DaggerItem((new Item.Properties().durability(242))));

}