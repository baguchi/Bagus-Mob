package baguchan.wasabi.registry;

import baguchan.wasabi.Wasabi;
import baguchan.wasabi.item.ShapedLeafItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Wasabi.MODID);
	public static final RegistryObject<Item> TENGU_SPAWNEGG = ITEM_REGISTRY.register("tengu_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.TENGU, 0x959B9B, 0xDFD96D, (new Item.Properties())));
	public static final RegistryObject<Item> SHARPED_LEAF = ITEM_REGISTRY.register("sharped_leaf", () -> new ShapedLeafItem((new Item.Properties().durability(128))));
}