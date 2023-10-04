package baguchan.bagusmob.entity.brain.behaviors;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class Pacified {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create((p_259944_) -> {
            return p_259944_.group(p_259944_.registered(MemoryModuleType.ATTACK_TARGET), p_259944_.present(MemoryModuleType.PACIFIED)).apply(p_259944_, p_259944_.point(() -> {
                return "[BecomePassive]";
            }, (p_260120_, p_259674_) -> {
                return (p_260328_, p_259412_, p_259725_) -> {
                    p_260120_.erase();
                    return true;
                };
            }));
        });
    }
}
