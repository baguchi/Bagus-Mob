package baguchan.tengu.registry;

import baguchan.tengu.TenguCore;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TenguItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TenguCore.MODID);
	public static final RegistryObject<Item> TENGU_SPAWNEGG = ITEM_REGISTRY.register("tengu_spawn_egg", () -> new ForgeSpawnEggItem(TenguEntityRegistry.TENGU, 0x959B9B, 0xDFD96D, (new Item.Properties())));
}