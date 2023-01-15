package baguchan.tengu.entity;

import baguchan.tengu.entity.goal.JumpTheSky;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public class Tengu extends AbstractIllager {


	public Tengu(EntityType<? extends Tengu> p_32105_, Level p_32106_) {
		super(p_32105_, p_32106_);
		this.xpReward = 10;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new JumpTheSky(this));
		this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0F, true));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.MAX_HEALTH, 28.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
	}

	public boolean tryToStartFallFlying() {
		if (!this.onGround && !this.isFallFlying() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION)) {
			return true;
		}

		return false;
	}

	public void startFallFlying() {
		this.setSharedFlag(7, true);
	}

	public void stopFallFlying() {
		this.setSharedFlag(7, true);
		this.setSharedFlag(7, false);
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
		} else {
			return false;
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.isFallFlying()) {
			this.setYRot(this.getYHeadRot());
		}
	}

	@Override
	public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

	}

	protected int calculateFallDamage(float p_149389_, float p_149390_) {
		return super.calculateFallDamage(p_149389_, p_149390_) - 10;
	}

}
