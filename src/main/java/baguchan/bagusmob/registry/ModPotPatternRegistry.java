package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotPatternRegistry {

    public static final DeferredRegister<DecoratedPotPattern> POT_PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERN, BagusMob.MODID);

    public static final Holder<DecoratedPotPattern> SNAKE = POT_PATTERNS.register("snake_pattern", () -> new DecoratedPotPattern(ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "snake_pattern")));

    public static void expandVanilla() {
        ImmutableMap.Builder<Item, ResourceKey<DecoratedPotPattern>> itemsToPot = new ImmutableMap.Builder<>();
        itemsToPot.putAll(DecoratedPotPatterns.ITEM_TO_POT_TEXTURE);
        itemsToPot.put(ModItemRegistry.SNAKE_POTTERY_SHERD.get(), create(SNAKE.getRegisteredName()));
        DecoratedPotPatterns.ITEM_TO_POT_TEXTURE = itemsToPot.build();
    }

    private static ResourceKey<DecoratedPotPattern> create(String p_272919_) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERN, ResourceLocation.parse(p_272919_));
    }
}