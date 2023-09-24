package baguchan.bagusmob.entity;

import baguchan.bagusmob.entity.goal.LockCastGoal;
import baguchan.bagusmob.entity.goal.SummonVilerVexCastGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Modifiger extends AbstractIllager {

    protected static final EntityDataAccessor<Boolean> IS_TIME_LOCK_CAST = SynchedEntityData.defineId(Modifiger.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_SUMMON_CAST = SynchedEntityData.defineId(Modifiger.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState lockSpellAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public final AnimationState summonSpellAnimationState = new AnimationState();
    private final int maxLockSpellTick = (int) 2.75f * 20;
    private int lockSpellTick = maxLockSpellTick;

    private final int maxSummonTick = (int) 2.75f * 20;
    private int summonSpellTick = maxSummonTick;

    public Modifiger(EntityType<? extends Modifiger> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 20;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_TIME_LOCK_CAST, false);
        this.entityData.define(IS_SUMMON_CAST, false);
    }

    @Override
    protected void tickDeath() {
        if (this.level().isClientSide()) {
            if (this.deathTime == 0) {
                this.deathAnimationState.start(this.tickCount);
                this.lockSpellAnimationState.stop();
            }
        }
        if (++this.deathTime >= 60 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        super.onSyncedDataUpdated(p_21104_);
        if (this.level().isClientSide()) {
            if (IS_TIME_LOCK_CAST.equals(p_21104_)) {
                if (this.isTimeLockCast()) {
                    this.lockSpellAnimationState.start(this.tickCount);
                    this.lockSpellTick = 0;
                } else {
                    if (this.lockSpellTick < this.maxSummonTick) {
                        this.lockSpellAnimationState.stop();
                    }
                }
            }
            if (IS_SUMMON_CAST.equals(p_21104_)) {
                if (this.isSummonCast()) {
                    this.summonSpellAnimationState.start(this.tickCount);
                    this.summonSpellTick = 0;
                }
            }
        }
    }

    public boolean isTimeLockCast() {
        return this.entityData.get(IS_TIME_LOCK_CAST);
    }

    public void setTimeLockCast(boolean p_37900_) {
        this.entityData.set(IS_TIME_LOCK_CAST, p_37900_);
    }

    public boolean isSummonCast() {
        return this.entityData.get(IS_SUMMON_CAST);
    }

    public void setSummonCast(boolean p_37900_) {
        this.entityData.set(IS_SUMMON_CAST, p_37900_);
    }


    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new LockCastGoal(this, (int) (1.5F * 20)));
        this.goalSelector.addGoal(5, new SummonVilerVexCastGoal(this, (int) 80));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.8D, 1.2D));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, AbstractGolem.class, 8.0F, 0.8D, 1.2D));


        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers(AbstractIllager.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));

    }

    @Override
    protected PathNavigation createNavigation(Level p_218342_) {
        GroundPathNavigation path = new GroundPathNavigation(this, p_218342_);
        path.setCanOpenDoors(true);
        path.setCanFloat(true);
        path.setCanPassDoors(true);
        return path;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide) {
            if (this.lockSpellTick < this.maxLockSpellTick) {
                this.lockSpellTick++;
            }

            if (this.lockSpellTick >= this.maxLockSpellTick) {
                this.lockSpellAnimationState.stop();
            }

            if (this.summonSpellTick < this.maxSummonTick) {
                this.summonSpellTick++;
            }

            if (this.summonSpellTick >= this.maxSummonTick) {
                this.summonSpellAnimationState.stop();
            }
        }
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_37856_, DifficultyInstance p_37857_, MobSpawnType p_37858_, @Nullable SpawnGroupData p_37859_, @Nullable CompoundTag p_37860_) {
        //this.buildingPos = Optional.of(this.blockPosition());
        return super.finalizeSpawn(p_37856_, p_37857_, p_37858_, p_37859_, p_37860_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 36.0D).add(Attributes.ARMOR, 6F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 8.0D).add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_33306_) {
        return SoundEvents.PILLAGER_HURT;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public boolean isAlliedTo(Entity p_33314_) {
        if (super.isAlliedTo(p_33314_)) {
            return true;
        } else if (p_33314_ instanceof LivingEntity && ((LivingEntity) p_33314_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_33314_.getTeam() == null;
        } else if (p_33314_ instanceof Vex) {
            return this.isAlliedTo(((Vex) p_33314_).getOwner());
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public IllagerArmPose getArmPose() {
        return this.isCelebrating() ? IllagerArmPose.CELEBRATING : IllagerArmPose.CROSSED;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_37870_) {
        super.addAdditionalSaveData(p_37870_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_37862_) {
        super.readAdditionalSaveData(p_37862_);
    }

    @Override
    public boolean canBeLeader() {
        return true;
    }
}
