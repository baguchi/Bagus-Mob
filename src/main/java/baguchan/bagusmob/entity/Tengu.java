package baguchan.bagusmob.entity;

import bagu_chan.bagus_lib.entity.goal.AnimatedAttackGoal;
import baguchan.bagusmob.entity.goal.JumpTheSkyGoal;
import baguchan.bagusmob.registry.ModItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Map;

public class Tengu extends AbstractIllager {

    public final AnimationState slashRightAnimationState = new AnimationState();
    public final AnimationState slashLeftAnimationState = new AnimationState();
    public final AnimationState fallAnimationState = new AnimationState();

    public boolean flyFailed;

    public Tengu(EntityType<? extends Tengu> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 10;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new TenguBodyRotationControl(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new JumpTheSkyGoal(this));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new AnimatedAttackGoal(this, 1.0F, 19, 19 - 12));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers(AbstractIllager.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 28.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
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
    public void addAdditionalSaveData(CompoundTag p_250330_) {
        super.addAdditionalSaveData(p_250330_);
        p_250330_.putBoolean("IsFallFlying", this.getPose() == Pose.FALL_FLYING);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_250781_) {
        super.readAdditionalSaveData(p_250781_);
        if (p_250781_.getBoolean("IsFallFlying")) {
            this.setPose(Pose.FALL_FLYING);
        }
    }

    public void startFallFlying() {
        this.setSharedFlag(7, true);
    }

    public void stopFallFlying() {
        this.setSharedFlag(7, true);
        this.setSharedFlag(7, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33306_) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public boolean isAlliedTo(Entity p_33314_) {
        if (super.isAlliedTo(p_33314_)) {
            return true;
        } else if (p_33314_ instanceof LivingEntity && ((LivingEntity) p_33314_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_33314_.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.updatePlayerPose();
    }

    protected void updatePlayerPose() {
        Pose pose;
        if (this.isFallFlying()) {
            pose = Pose.FALL_FLYING;
        } else {
            pose = Pose.STANDING;
        }

        Pose pose1 = pose;

        this.setPose(pose1);
    }

    public float getStandingEyeHeight(Pose p_36259_, EntityDimensions p_36260_) {
        switch (p_36259_) {
            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK:
                return 0.4F;
            case CROUCHING:
                return 1.27F;
            default:
                return 1.62F;
        }
    }

    public EntityDimensions getDimensions(Pose p_36166_) {
        if (p_36166_ == Pose.FALL_FLYING) {
            return EntityDimensions.scalable(0.8F, 0.8F);
        }
        return super.getDimensions(p_36166_);
    }


    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isFallFlying()) {
            if (this.isInFluidType() || this.isInPowderSnow) {
                this.stopFallFlying();
            }
            if (!this.flyFailed && this.hurtTime > 0) {
                this.stopFallFlying();
                this.flyFailed = true;
                if (this.level().isClientSide()) {
                    this.fallAnimationState.start(this.tickCount);
                }
            }
        }


        if (!this.flyFailed) {
            if (!this.isFallFlying() && !this.isInFluidType() && this.fallDistance > 3.0F) {
                this.startFallFlying();
            }
        }


        if (this.flyFailed) {
            if (this.onGround() || this.isInFluidType()) {
                if (this.level().isClientSide()) {
                    this.fallAnimationState.stop();
                }
                this.flyFailed = false;
            }
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        this.populateDefaultEquipmentEnchantments(randomsource, p_34089_);
        return spawngroupdata;
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219149_, DifficultyInstance p_219150_) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItemRegistry.SHARPED_LEAF.get()));
        }
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {
        ItemStack itemstack = new ItemStack(ModItemRegistry.SHARPED_LEAF.get());
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (p_37844_ > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemstack);
        }

        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        return this.isCelebrating() ? IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.CROSSED;
    }

    protected int calculateFallDamage(float p_149389_, float p_149390_) {
        if (this.flyFailed) {
            return super.calculateFallDamage(p_149389_, p_149390_) + 4;
        }

        return super.calculateFallDamage(p_149389_, p_149390_) - 10;
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if (this.isHolding(is -> is.is(ModItemRegistry.SHARPED_LEAF.get()))) {
            SlashAir slashAir = new SlashAir(this.level(), this);
            double d1 = p_21372_.getX() - this.getX();
            double d2 = p_21372_.getEyeY() - this.getEyeY();
            double d3 = p_21372_.getZ() - this.getZ();
            slashAir.damage = 1.0F;
            slashAir.shoot(d1, d2, d3, 1.2F, 0.8F);

            this.level().addFreshEntity(slashAir);
        }
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public void handleEntityEvent(byte p_219360_) {
        if (p_219360_ == 4) {
            if (this.isLeftHanded()) {
                this.slashLeftAnimationState.start(this.tickCount);
            } else {
                this.slashRightAnimationState.start(this.tickCount);
            }
        } else {
            super.handleEntityEvent(p_219360_);
        }

    }

    class TenguBodyRotationControl extends BodyRotationControl {
        public TenguBodyRotationControl(Mob p_33216_) {
            super(p_33216_);
        }

        public void clientTick() {
            if (Tengu.this.isFallFlying()) {
                super.clientTick();
                Tengu.this.setYRot(Tengu.this.getYHeadRot());
                Tengu.this.setYBodyRot(Tengu.this.getYHeadRot());
            } else {
                super.clientTick();
            }
        }
    }
}
