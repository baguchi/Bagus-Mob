package baguchan.wasabi.entity.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FlyingRandomStrollGoal extends RandomStrollGoal {
	public FlyingRandomStrollGoal(PathfinderMob p_25734_, double p_25735_) {
		super(p_25734_, p_25735_, 10);
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
				return false;
			} else {
				this.wantedX = vec3.x;
				this.wantedY = vec3.y;
				this.wantedZ = vec3.z;
				this.forceTrigger = false;
				return true;
			}
		}
	}

	@Nullable
	private Vec3 getTargetFlyPos(PathfinderMob p_260316_, int p_259038_, int p_259696_) {
		Vec3 vec3 = p_260316_.getViewVector(0.0F);

		Vec3 path = AirAndWaterRandomPos.getPos(p_260316_, p_259038_, p_259696_, -2, vec3.x, vec3.z, (double) ((float) Math.PI / 2F));

		Vec3 vec32 = HoverRandomPos.getPos(p_260316_, p_259038_, p_259696_, vec3.x, vec3.z, ((float) Math.PI / 2F), 3, 1);


		RandomSource randomsource = p_260316_.getRandom();
		double d0 = p_260316_.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
		double d1 = p_260316_.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
		double d2 = p_260316_.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);

		if (vec32 != null) {
			return vec32;
		}
		return path != null ? path : new Vec3(d0, Math.max(160, d1), d2);
	}
}
