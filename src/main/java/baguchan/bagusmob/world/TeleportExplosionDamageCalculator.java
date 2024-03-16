package baguchan.bagusmob.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TeleportExplosionDamageCalculator extends ExplosionDamageCalculator {
    public boolean shouldBlockExplode(Explosion p_46094_, BlockGetter p_46095_, BlockPos p_46096_, BlockState p_46097_, float p_46098_) {
        return false;
    }

    public boolean shouldDamageEntity(Explosion p_314652_, Entity p_314454_) {
        return p_314454_.isAttackable();
    }

    public float getEntityDamageAmount(Explosion p_311793_, Entity p_311929_) {
        float f = p_311793_.radius() * 2.0F;
        Vec3 vec3 = p_311793_.center();
        double d0 = Math.sqrt(p_311929_.distanceToSqr(vec3)) / (double) f;
        double d1 = (1.0 - d0) * (double) Explosion.getSeenPercent(vec3, p_311929_);
        return (float) ((d1 * d1 + d1) / 2.0 * 7.0 * (double) f + 1.0) * 0.75F;
    }
}
