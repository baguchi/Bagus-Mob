package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.effect.TimeLockEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BagusMob.MODID);

    public static final RegistryObject<MobEffect> TIME_LOCK = MOB_EFFECTS.register("time_lock", () -> new TimeLockEffect(MobEffectCategory.NEUTRAL, 0xDA784A));

}
