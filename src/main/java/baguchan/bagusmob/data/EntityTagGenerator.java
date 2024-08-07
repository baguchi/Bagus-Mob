package baguchan.bagusmob.data;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.registry.ModEntityRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends EntityTypeTagsProvider {
    public EntityTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, BagusMob.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(EntityTypeTags.ILLAGER).add(ModEntityRegistry.NINJAR.get()).add(ModEntityRegistry.TENGU.get());
        this.tag(EntityTypeTags.RAIDERS).add(ModEntityRegistry.NINJAR.get()).add(ModEntityRegistry.TENGU.get());

        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(ModEntityRegistry.SOILTH.get());
    }

}

