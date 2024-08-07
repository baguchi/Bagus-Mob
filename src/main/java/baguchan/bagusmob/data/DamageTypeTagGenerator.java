package baguchan.bagusmob.data;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.registry.ModDamageSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends DamageTypeTagsProvider {
    public DamageTypeTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, BagusMob.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(DamageTypeTags.NO_KNOCKBACK).add(ModDamageSource.SOUL);
        this.tag(DamageTypeTags.WITCH_RESISTANT_TO).add(ModDamageSource.SOUL);
        this.tag(DamageTypeTags.PANIC_CAUSES).add(ModDamageSource.SOUL).add(ModDamageSource.SPIN_BLADE);
    }

}

