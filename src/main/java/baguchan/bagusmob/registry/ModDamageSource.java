package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public interface ModDamageSource {
    ResourceKey<DamageType> SPIN_BLADE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "spin_blade"));
    ResourceKey<DamageType> SOUL = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "soul"));

}
