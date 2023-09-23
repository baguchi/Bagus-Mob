package baguchan.bagusmob.entity.goal;

import baguchan.bagusmob.entity.Modifiger;
import baguchan.bagusmob.entity.VilerVex;
import baguchan.bagusmob.registry.ModEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Vex;

import java.util.EnumSet;

public class SummonVilerVexCastGoal extends Goal {
    private final TargetingConditions vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();

    private final Modifiger mob;
    private static final UniformInt TIME_BETWEEN_COOLDOWN = UniformInt.of(200, 600);

    private int cooldown;
    private int tick;
    private final int maxTick;

    public SummonVilerVexCastGoal(Modifiger p_25919_, int maxTick) {
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
            if (livingEntity != null && livingEntity.isAlive() && this.mob.hasLineOfSight(livingEntity)) {
                this.cooldown = TIME_BETWEEN_COOLDOWN.sample(this.mob.getRandom());
                int i = this.mob.level().getNearbyEntities(Vex.class, this.vexCountTargeting, this.mob, this.mob.getBoundingBox().inflate(16.0D)).size();
                return this.mob.getRandom().nextInt(2) + 1 > i;
            }
        }

        return false;
    }

    public void start() {
        this.mob.getNavigation().stop();
        this.tick = 0;
        this.mob.setSummonCast(true);
        this.mob.playSound(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

    @Override
    public void stop() {
        super.stop();
        ServerLevel serverlevel = (ServerLevel) this.mob.level();

        for (int i = 0; i < 3; ++i) {
            BlockPos blockpos = this.mob.blockPosition().offset(-2 + this.mob.getRandom().nextInt(5), 1, -2 + this.mob.getRandom().nextInt(5));
            VilerVex vex = ModEntityRegistry.VILER_VEX.get().create(this.mob.level());
            if (vex != null) {
                vex.moveTo(blockpos, 0.0F, 0.0F);
                vex.finalizeSpawn(serverlevel, this.mob.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                vex.setOwner(this.mob);
                vex.setBoundOrigin(blockpos);
                vex.setLimitedLife(20 * (50 + this.mob.getRandom().nextInt(120)));
                serverlevel.addFreshEntityWithPassengers(vex);
            }
        }

        this.mob.setSummonCast(false);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.tick < this.maxTick;
    }

    public void tick() {
        ++tick;
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null && livingEntity.isAlive()) {
            this.mob.getLookControl().setLookAt(livingEntity, 30F, 30F);
        }
    }
}