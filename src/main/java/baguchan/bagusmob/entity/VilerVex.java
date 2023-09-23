package baguchan.bagusmob.entity;

import baguchan.bagusmob.entity.goal.FlyingCrossbowAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class VilerVex extends Vex implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(VilerVex.class, EntityDataSerializers.BOOLEAN);

    public VilerVex(EntityType<? extends Vex> p_33984_, Level p_33985_) {
        super(p_33984_, p_33985_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ARMOR, 3D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FlyingCrossbowAttackGoal<>(this, 10));
        this.goalSelector.addGoal(4, new VexBackGoal());
        this.goalSelector.addGoal(8, new VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new VexCopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219135_, DifficultyInstance p_219136_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_33280_) {
        return p_33280_ == Items.CROSSBOW;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean p_33302_) {
        this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
    }

    @Override
    public void performRangedAttack(LivingEntity p_33272_, float p_33273_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_33275_, ItemStack p_33276_, Projectile p_33277_, float p_33278_) {
        this.shootCrossbowProjectile(this, p_33275_, p_33277_, p_33278_, 1.6F);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }


    public float getWalkTargetValue(BlockPos p_33895_, LevelReader p_33896_) {
        if (p_33896_.getBlockState(p_33895_).isAir()) {
            return 10.0F;
        } else if (!p_33896_.getBlockState(p_33895_).getFluidState().isEmpty()) {
            return 0.0F;
        } else {
            return -10.0F;
        }
    }

    class VexBackGoal extends Goal {

        public Vec3 vec3Target = Vec3.ZERO;

        public VexBackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = VilerVex.this.getTarget();
            if (livingentity != null && livingentity.isAlive() && !VilerVex.this.getMoveControl().hasWanted()) {
                return true;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return VilerVex.this.getMoveControl().hasWanted() && VilerVex.this.getTarget() != null && VilerVex.this.getTarget().isAlive();
        }

        public void start() {
            vec3Target = DefaultRandomPos.getPosAway(VilerVex.this, 16, 7, VilerVex.this.position());

            VilerVex.this.setIsCharging(true);
        }

        public void stop() {
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = VilerVex.this.getTarget();
            if (livingentity != null) {
                if (VilerVex.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    VilerVex.this.doHurtTarget(livingentity);
                    VilerVex.this.setIsCharging(false);
                } else {
                    double d0 = VilerVex.this.distanceToSqr(livingentity);

                    if (vec3Target != null) {
                        VilerVex.this.moveControl.setWantedPosition(vec3Target.x, vec3Target.y, vec3Target.z, 0.3D);
                    }
                    if (d0 < 8F * 8F) {

                        if (VilerVex.this.random.nextFloat() < 0.01F) {
                            vec3Target = DefaultRandomPos.getPosAway(VilerVex.this, 6, 6, livingentity.position());
                        }
                    } else {
                        vec3Target = DefaultRandomPos.getPosTowards(VilerVex.this, 6, 6, livingentity.position(), (double) ((float) Math.PI / 2F));

                    }
                }

            }
        }
    }

    class VexCopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

        public VexCopyOwnerTargetGoal(PathfinderMob p_34056_) {
            super(p_34056_, false);
        }

        public boolean canUse() {
            return VilerVex.this.getOwner() != null && VilerVex.this.getOwner().getTarget() != null && this.canAttack(VilerVex.this.getOwner().getTarget(), this.copyOwnerTargeting);
        }

        public void start() {
            VilerVex.this.setTarget(VilerVex.this.getOwner().getTarget());
            super.start();
        }
    }

    public class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !VilerVex.this.getMoveControl().hasWanted() && VilerVex.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = VilerVex.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = VilerVex.this.blockPosition();
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(VilerVex.this.random.nextInt(15) - 7, VilerVex.this.random.nextInt(11) - 5, VilerVex.this.random.nextInt(15) - 7);
                if (VilerVex.this.level().isEmptyBlock(blockpos1)) {
                    VilerVex.this.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                    if (VilerVex.this.getTarget() == null) {
                        VilerVex.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
