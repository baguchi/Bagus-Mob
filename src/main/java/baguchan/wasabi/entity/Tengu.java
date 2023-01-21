package baguchan.wasabi.entity;

import baguchan.wasabi.entity.goal.JumpTheSky;
import baguchan.wasabi.registry.ModItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class Tengu extends AbstractIllager {

	public final AnimationState slashRightAnimationState = new AnimationState();
	public final AnimationState slashLeftAnimationState = new AnimationState();

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
		this.goalSelector.addGoal(1, new JumpTheSky(this));
		this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new TenguMeleeAttackGoal());
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
			if (this.isInFluidType() || this.isInPowderSnow) {
				this.stopFallFlying();
			}
		}

		if (!this.isFallFlying() && !this.isInFluidType() && this.fallDistance > 3.0F) {
			this.startFallFlying();
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
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItemRegistry.SHARPED_LEAF.get()));
	}

	@Override
	public AbstractIllager.IllagerArmPose getArmPose() {
		return this.isCelebrating() ? IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.CROSSED;
	}

	protected int calculateFallDamage(float p_149389_, float p_149390_) {
		return super.calculateFallDamage(p_149389_, p_149390_) - 10;
	}

	@Override
	public boolean doHurtTarget(Entity p_21372_) {
		if (this.isHolding(is -> is.is(ModItemRegistry.SHARPED_LEAF.get()))) {
			SlashAir slashAir = new SlashAir(this.level, this);
			double d1 = p_21372_.getX() - this.getX();
			double d2 = p_21372_.getEyeY() - this.getEyeY();
			double d3 = p_21372_.getZ() - this.getZ();
			slashAir.damage = 1.0F;
			slashAir.shoot(d1, d2, d3, 1.2F, 0.8F);

			this.level.addFreshEntity(slashAir);
		}
		return super.doHurtTarget(p_21372_);
	}

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

	class TenguMeleeAttackGoal extends MeleeAttackGoal {
		public TenguMeleeAttackGoal() {
			super(Tengu.this, 1.0D, true);
		}

		protected void checkAndPerformAttack(LivingEntity p_29589_, double p_29590_) {
			double d0 = this.getAttackReachSqr(p_29589_);
			if (p_29590_ <= d0 && this.getTicksUntilNextAttack() <= 12) {
				this.resetAttackCooldown();
				this.mob.doHurtTarget(p_29589_);
			} else if (p_29590_ <= d0) {
				if (this.isTimeToAttack()) {
					this.resetAttackCooldown();
				}

				if (this.getTicksUntilNextAttack() == 16) {
					Tengu.this.level.broadcastEntityEvent(Tengu.this, (byte) 4);
				}
			} else {
				this.resetAttackCooldown();
			}

		}
	}

	class TenguBodyRotationControl extends BodyRotationControl {
		public TenguBodyRotationControl(Mob p_33216_) {
			super(p_33216_);
		}

		public void clientTick() {
			if (Tengu.this.isFallFlying()) {
				Tengu.this.setYRot(Tengu.this.getYHeadRot());
				Tengu.this.setYBodyRot(Tengu.this.getYHeadRot());
			} else {
				super.clientTick();
			}
		}
	}
}
