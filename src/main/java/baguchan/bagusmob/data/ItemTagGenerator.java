package baguchan.bagusmob.data;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper exFileHelper) {
        super(packOutput, lookupProvider, provider, BagusMob.MODID, exFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        this.tag(ItemTags.DECORATED_POT_SHERDS).add(ModItemRegistry.SNAKE_POTTERY_SHERD.get());
        this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(ModItemRegistry.SHARPED_LEAF.get()).add(ModItemRegistry.BEAST_CUDGEL.get()).add(ModItemRegistry.DAGGER.get());
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModItemRegistry.SHARPED_LEAF.get()).add(ModItemRegistry.BEAST_CUDGEL.get()).add(ModItemRegistry.DAGGER.get());
        this.tag(ItemTags.HEAD_ARMOR).add(ModItemRegistry.NINJA_HOOD.get());
        this.tag(ItemTags.CHEST_ARMOR).add(ModItemRegistry.NINJA_CHESTPLATE.get());
        this.tag(ItemTags.FOOT_ARMOR).add(ModItemRegistry.NINJA_BOOTS.get());
    }
}
