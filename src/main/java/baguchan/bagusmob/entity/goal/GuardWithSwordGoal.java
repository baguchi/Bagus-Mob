package baguchan.bagusmob.entity.goal;

import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class GuardWithSwordGoal extends Goal {
    private final PathfinderMob mob;

    public GuardWithSwordGoal(PathfinderMob p_25919_) {
        this.mob = p_25919_;
    }

    @Override
    public boolean canUse() {
        return this.mob.isHolding(ModItemRegistry.KATANA.get()) && this.mob.getTarget() != null && !this.mob.isWithinMeleeAttackRange(this.mob.getTarget());
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isUsingItem() && this.mob.getTarget() != null && !this.mob.isWithinMeleeAttackRange(this.mob.getTarget());
    }

    public void start() {
        this.mob.startUsingItem(InteractionHand.MAIN_HAND);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.stopUsingItem();
    }
}