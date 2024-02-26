package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPotPatternRegistry {

    public static final DeferredRegister<String> POT_PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERNS, BagusMob.MODID);

    public static final Supplier<String> SNAKE = POT_PATTERNS.register("snake_pattern", () -> BagusMob.MODID + ":snake_pattern");

    public static void expandVanilla() {
        ImmutableMap.Builder<Item, ResourceKey<String>> itemsToPot = new ImmutableMap.Builder<>();
        itemsToPot.putAll(DecoratedPotPatterns.ITEM_TO_POT_TEXTURE);
        itemsToPot.put(ModItemRegistry.SNAKE_POTTERY_SHERD.get(), create(SNAKE.get()));
        DecoratedPotPatterns.ITEM_TO_POT_TEXTURE = itemsToPot.build();
    }

    private static ResourceKey<String> create(String p_272919_) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERNS, new ResourceLocation(p_272919_));
    }
}