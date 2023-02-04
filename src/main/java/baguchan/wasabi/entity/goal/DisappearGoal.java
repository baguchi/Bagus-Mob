package baguchan.wasabi.entity.goal;

import baguchan.wasabi.entity.Ninjar;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class DisappearGoal extends Goal {
	private final Ninjar mob;

	private int tick;
	private final int maxTick;

	public DisappearGoal(Ninjar p_25919_, int maxTick) {
		this.mob = p_25919_;
		this.maxTick = maxTick;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		return this.mob.getPose() == Pose.DIGGING;
	}

	public void start() {
		this.mob.getNavigation().stop();
		this.tick = 0;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		if (++tick > this.maxTick) {
			this.mob.setPose(Pose.EMERGING);
			this.mob.level.explode(this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
			for (int i = 0; i < 6; i++) {
				if (this.mob.teleport()) {
					break;
				}
			}
		}
	}
}