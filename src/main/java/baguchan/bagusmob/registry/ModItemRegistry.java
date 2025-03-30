package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.item.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItemRegistry {
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, BagusMob.MODID);
    public static final Supplier<Item> TENGU_SPAWNEGG = ITEM_REGISTRY.register("tengu_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.TENGU, 0x959B9B, 0xDFD96D, (new Item.Properties())));
    public static final Supplier<Item> NINJAR_SPAWNEGG = ITEM_REGISTRY.register("ninjar_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.NINJAR, 0x323237, 0x959B9B, (new Item.Properties())));
    public static final Supplier<Item> SHARPED_LEAF = ITEM_REGISTRY.register("sharped_leaf", () -> new ShapedLeafItem((new Item.Properties().attributes(ShapedLeafItem.createAttributes()).durability(128))));
    public static final Supplier<Item> DAGGER = ITEM_REGISTRY.register("dagger", () -> new DaggerItem((new Item.Properties().attributes(DaggerItem.createAttributes()).durability(242))));

    public static final Supplier<Item> NINJA_BOOTS = ITEM_REGISTRY.register("ninja_boots", () -> new NinjaArmorItem(ArmorItem.Type.BOOTS, (new Item.Properties().durability(162))));
    public static final Supplier<Item> NINJA_CHESTPLATE = ITEM_REGISTRY.register("ninja_chestplate", () -> new NinjaArmorItem(ArmorItem.Type.CHESTPLATE, (new Item.Properties().durability(214))));
    public static final Supplier<Item> NINJA_HOOD = ITEM_REGISTRY.register("ninja_hood", () -> new NinjaArmorItem(ArmorItem.Type.HELMET, (new Item.Properties().durability(182))));
    public static final Supplier<Item> RUDEHOG_SPAWNEGG = ITEM_REGISTRY.register("rudehog_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.RUDEHOG, 0x85424C, 0x361D12, (new Item.Properties())));
    public static final Supplier<Item> HUNTER_BOAR_SPAWNEGG = ITEM_REGISTRY.register("hunter_boar_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.HUNTER_BOAR, 0x85424C, 0xFCED6B, (new Item.Properties())));
    public static final Supplier<Item> BURNER_HOG_SPAWNEGG = ITEM_REGISTRY.register("burner_hog_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.BURNER_HOG, 0xF2BA86, 0x545054, (new Item.Properties())));
    public static final Supplier<Item> POT_SNAKE_SPAWNEGG = ITEM_REGISTRY.register("pot_snake_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityRegistry.POT_SNAKE, 0x1E4516, 0x72A268, (new Item.Properties())));

    public static final Supplier<Item> SNAKE_POTTERY_SHERD = ITEM_REGISTRY.register("snake_pottery_sherd", () -> new Item((new Item.Properties())));

    public static final Supplier<Item> BEAST_CUDGEL = ITEM_REGISTRY.register("beast_cudgel", () -> new BeastWeaponItem((new Item.Properties()).durability(520).attributes(BeastWeaponItem.createAttributes()).rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> SPIN_BLADE = ITEM_REGISTRY.register("spin_blade", () -> new SpinBladeItem((new Item.Properties()).durability(320).rarity(Rarity.UNCOMMON)));

	@OnlyIn(Dist.CLIENT)
	public static void addItemModelProperties() {

        ItemProperties.register(SPIN_BLADE.get(), ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "thrown"), (stack, world, entity, idk) ->
				SpinBladeItem.getThrownUuid(stack) != null ? 1 : 0);
    }

}