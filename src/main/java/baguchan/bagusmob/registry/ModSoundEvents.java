package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BagusMob.MODID);

    public static final Supplier<SoundEvent> NINJAR_TELEPORT = createEvent("mob.ninjar.teleport");

    private static Supplier<SoundEvent> createEvent(String sound) {
        ResourceLocation name = new ResourceLocation(BagusMob.MODID, sound);
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(name));
    }
}