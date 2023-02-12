package baguchan.wasabi.entity.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FlyingRandomStrollGoal extends RandomStrollGoal {
	public FlyingRandomStrollGoal(PathfinderMob p_25734_, double p_25735_) {
		super(p_25734_, p_25735_, 20);
	}

	@Override
	public boolean canUse() {
		if (this.mob.isVehicle() || !this.mob.isFallFlying()) {
			return false;
		} else {
			if (!this.forceTrigger) {
				if (this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
					return false;
				}
			}

			Vec3 vec3 = this.getTargetFlyPos(this.mob, 15, 10);
			if (vec3 == null) {
				Vec3 vec32 = this.getPosition();
				if (vec32 == null) {
					return false;
				} else {
					this.wantedX = vec32.x;
					this.wantedY = vec32.y;
					this.wantedZ = vec32.z;
					this.forceTrigger = false;
					return true;
				}
			} else {
				this.wantedX = vec3.x;
				this.wantedY = vec3.y;
				this.wantedZ = vec3.z;
				this.forceTrigger = false;
				return true;
			}
		}
	}

	@Override
	protected Vec3 getPosition() {
		Vec3 vec3 = this.mob.getViewVector(0.0F);
		int i = 8;

		RandomSource randomsource = this.mob.getRandom();
		double d0 = this.mob.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
		double d1 = this.mob.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
		double d2 = this.mob.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);

		if (this.mob.getY() > 160) {
			d1 = this.mob.getY() - (double) ((randomsource.nextFloat() * 1.2F) * 16.0F);
		}

		if (this.mob.getY() > 12) {
			d1 = this.mob.getY() + (double) ((randomsource.nextFloat() * 5.0F) * 16.0F);
		}

		return new Vec3(d0, d1, d2);
	}

	@Nullable
	private Vec3 getTargetFlyPos(PathfinderMob p_260316_, int p_259038_, int p_259696_) {
		Vec3 vec3 = p_260316_.getViewVector(0.0F);
		return AirAndWaterRandomPos.getPos(p_260316_, p_259038_, p_259696_, -2, vec3.x, vec3.z, (double) ((float) Math.PI / 2F));
	}
}
