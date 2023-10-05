package baguchan.bagusmob.entity.brain.behaviors;

import baguchan.bagusmob.entity.BurnerHog;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class BurnerActive<E extends BurnerHog> extends Behavior<E> {
    protected int ticks;

    public BurnerActive() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel p_22538_, E p_22539_) {
        return p_22539_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent();
    }

    @Override
    protected boolean canStillUse(ServerLevel p_22545_, E p_22546_, long p_22547_) {
        return p_22546_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent();
    }

    @Override
    protected void start(ServerLevel p_22540_, E p_22541_, long p_22542_) {
        super.start(p_22540_, p_22541_, p_22542_);
        p_22541_.setCharge(true);
    }

    @Override
    protected void stop(ServerLevel p_22548_, E p_22549_, long p_22550_) {
        super.stop(p_22548_, p_22549_, p_22550_);
        p_22549_.setCharge(false);
    }

    @Override
    protected void tick(ServerLevel p_22551_, E p_22552_, long p_22553_) {
        super.tick(p_22551_, p_22552_, p_22553_);
        Optional<LivingEntity> optional = p_22552_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);

        if (optional.isPresent()) {
            LivingEntity livingEntity = optional.get();
            p_22552_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(optional.get(), true));
            if (p_22552_.getSensing().hasLineOfSight(optional.get())) {
                if (this.ticks == 40) {
                    p_22552_.playSound(SoundEvents.FIRE_EXTINGUISH);
                }
                if (++this.ticks >= 60 && this.ticks <= 70 && this.ticks % 5 == 0) {
                    Vec3 vec3 = livingEntity.getDeltaMovement();
                    double d0 = livingEntity.getX() + vec3.x - p_22552_.getX();
                    double d1 = livingEntity.getEyeY() - p_22552_.getEyeY();
                    double d2 = livingEntity.getZ() + vec3.z - p_22552_.getZ();
                    double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                    double d4 = Math.sqrt(Math.sqrt(d3)) * 0.5D;
                    for (int i = 0; i < 2; i++) {
                        SmallFireball entity = new SmallFireball(p_22551_, p_22552_, p_22552_.getRandom().triangle(d0, 2.5D * d4), p_22552_.getRandom().triangle(d1, 2.5D * d4), p_22552_.getRandom().triangle(d2, 2.5D * d4));
                        entity.setPos(p_22552_.position().x(), p_22552_.getEyeY() - 0.1F, p_22552_.position().z());
                        p_22551_.addFreshEntity(entity);
                    }
                    p_22552_.playSound(SoundEvents.FIRECHARGE_USE);
                }
                if (this.ticks > 80) {
                    this.ticks = 0;
                }
            }
        } else {
            this.ticks = 0;
        }
    }
}