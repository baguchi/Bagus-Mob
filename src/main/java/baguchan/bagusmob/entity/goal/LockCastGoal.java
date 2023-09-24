package baguchan.bagusmob.entity.goal;

import baguchan.bagusmob.entity.Modifiger;
import baguchan.bagusmob.registry.ModEffects;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class LockCastGoal extends Goal {
    private final Modifiger mob;
    private static final UniformInt TIME_BETWEEN_COOLDOWN = UniformInt.of(400, 800);

    private int cooldown;
    private int tick;
    private final int maxTick;

    public LockCastGoal(Modifiger p_25919_, int maxTick) {
        this.mob = p_25919_;
        this.maxTick = maxTick;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (this.cooldown > 0) {
            --this.cooldown;
        }
        if (this.cooldown <= 0) {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity != null && livingEntity.isAlive() && this.mob.hasLineOfSight(livingEntity) && this.mob.distanceToSqr(livingEntity) < 64.0F) {
                this.cooldown = TIME_BETWEEN_COOLDOWN.sample(this.mob.getRandom());
                return true;
            }
        }

        return false;
    }

    public void start() {
        this.mob.getNavigation().stop();
        this.tick = 0;
        this.mob.setTimeLockCast(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setTimeLockCast(false);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.tick < this.maxTick && this.mob.hurtTime <= 0;
    }

    public void tick() {
        ++tick;
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null && livingEntity.isAlive() && this.mob.getSensing().hasLineOfSight(livingEntity)) {
            this.mob.getLookControl().setLookAt(livingEntity, 30F, 30F);
        }
        if (this.tick >= this.maxTick) {
            if (livingEntity != null && livingEntity.isAlive() && this.mob.hasLineOfSight(livingEntity)) {
                livingEntity.addEffect(new MobEffectInstance(ModEffects.TIME_LOCK.get(), 100 - 8 * 4), this.mob);
            }
        }
    }
}