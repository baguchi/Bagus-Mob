package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.entity.brain.RudeHogSensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, BagusMob.MODID);

    public static final Supplier<SensorType<RudeHogSensor>> RUDEHOG_SENSOR = SENSOR_TYPES.register("rudehog_sensor", () -> new SensorType<>(RudeHogSensor::new));

}
