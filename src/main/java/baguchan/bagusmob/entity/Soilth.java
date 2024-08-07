package baguchan.bagusmob.entity;

import baguchan.bagusmob.entity.projectile.SoulProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class Soilth extends Monster {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Soilth.class, EntityDataSerializers.BYTE);

    public final AnimationState chargeAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState deadAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();

    public Soilth(EntityType<? extends Soilth> p_32219_, Level p_32220_) {
        super(p_32219_, p_32220_);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.xpReward = 10;
    }

    @Override
    protected PathNavigation createNavigation(Level p_218342_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void travel(Vec3 p_218382_) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, p_218382_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, p_218382_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                this.moveRelative(this.getSpeed(), p_218382_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326216_) {
        super.defineSynchedData(p_326216_);
        p_326216_.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if (p_21375_ == 61) {
            this.shootAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(p_21375_);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.chargeAnimationState.animateWhen(this.isCharged(), this.tickCount);
    }

    @Override
    protected void tickDeath() {
        if (this.level().isClientSide() && this.deathTime == 0) {
            this.deadAnimationState.start(this.tickCount);
        }
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.03, 0));
        if (this.deathTime == 20) {
            this.playSound(SoundEvents.SOUL_SOIL_PLACE);
        }

        this.deathTime++;
        if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(4, new AttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.FLYING_SPEED, 0.1F)
                .add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.ARMOR, 3.0).add(Attributes.MAX_HEALTH, 16.0).add(Attributes.FOLLOW_RANGE, 24.0);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_32235_) {
        return SoundEvents.BLAZE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    public void aiStep() {
        if (this.isAlive()) {
            if (this.level().isClientSide) {
                if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                    for (int i = 0; i < 4; ++i) {
                        this.level().addParticle(ParticleTypes.SOUL, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
                    }
                    this.level().playLocalSound(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5, SoundEvents.SOUL_ESCAPE.value(), this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
                }
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
        }

        super.aiStep();
    }


    public boolean isCharged() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setCharged(boolean p_32241_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_32241_) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    static class AttackGoal extends Goal {
        private final Soilth soilth;
        private int attackStep;
        private int attackTime;
        private int lastSeen;

        public AttackGoal(Soilth p_32247_) {
            this.soilth = p_32247_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.soilth.getTarget();
            return livingentity != null && livingentity.isAlive() && this.soilth.canAttack(livingentity);
        }

        public void start() {
            this.attackStep = 0;
        }

        public void stop() {
            this.soilth.setCharged(false);
            this.lastSeen = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --this.attackTime;
            LivingEntity livingentity = this.soilth.getTarget();
            if (livingentity != null) {
                boolean flag = this.soilth.getSensing().hasLineOfSight(livingentity);
                if (flag) {
                    this.lastSeen = 0;
                } else {
                    ++this.lastSeen;
                }

                double d0 = this.soilth.distanceToSqr(livingentity);
                if (d0 < 4.0) {
                    if (!flag) {
                        return;
                    }

                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.soilth.doHurtTarget(livingentity);
                    }

                    this.soilth.getNavigation().moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0);
                } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                    double d1 = livingentity.getX() - this.soilth.getX();
                    double d2 = livingentity.getY(0.5) - this.soilth.getY(0.5);
                    double d3 = livingentity.getZ() - this.soilth.getZ();
                    if (this.attackStep == 1) {
                        if (this.attackTime == 5) {
                            this.soilth.level().broadcastEntityEvent(this.soilth, (byte) 61);
                        }
                    }

                    if (this.attackTime <= 0) {
                        ++this.attackStep;
                        if (this.attackStep == 1) {
                            this.attackTime = 60;
                            this.soilth.setCharged(true);
                        } else if (this.attackStep <= 4) {
                            this.attackTime = 8;
                        } else {
                            this.attackTime = 100;
                            this.attackStep = 0;
                            this.soilth.setCharged(false);
                        }

                        if (this.attackStep > 1) {
                            double d4 = Math.sqrt(Math.sqrt(d0)) * 0.5;
                            if (!this.soilth.isSilent()) {
                                this.soilth.level().levelEvent((Player) null, 1018, this.soilth.blockPosition(), 0);
                            }

                            for (int i = 0; i < 1; ++i) {
                                SoulProjectile soulProjectile = new SoulProjectile(this.soilth.level(), this.soilth);
                                double dx = livingentity.getX() - this.soilth.getX();
                                double dy = livingentity.getEyeY() - this.soilth.getEyeY();
                                double dz = livingentity.getZ() - this.soilth.getZ();
                                soulProjectile.shoot(dx, dy, dz, 0.8F, 0.8F);
                                this.soilth.level().addFreshEntity(soulProjectile);
                            }
                        }
                    }

                    this.soilth.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
                } else if (this.lastSeen < 5) {
                    this.soilth.getNavigation().moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0);
                }

                super.tick();
            }

        }

        private double getFollowDistance() {
            return this.soilth.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
