package baguchan.wasabi.entity;

import baguchan.wasabi.entity.goal.FlyingRandomStrollGoal;
import baguchan.wasabi.entity.goal.JumpTheSkyGoal;
import baguchan.wasabi.registry.ModItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Map;

public class Tengu extends AbstractIllager {
	public final AnimationState slashRightAnimationState = new AnimationState();
	public final AnimationState slashLeftAnimationState = new AnimationState();

	protected final FlyingPathNavigation flyingNavigation;
	protected final GroundPathNavigation groundNavigation;

	public Tengu(EntityType<? extends Tengu> p_32105_, Level p_32106_) {
		super(p_32105_, p_32106_);
		this.xpReward = 10;
		this.moveControl = new TenguMoveControl(this);
		this.flyingNavigation = new FlyingPathNavigation(this, p_32106_);
		this.groundNavigation = new GroundPathNavigation(this, p_32106_);
		this.flyingNavigation.setCanOpenDoors(true);
		this.groundNavigation.setCanOpenDoors(true);
	}

	@Override
	protected BodyRotationControl createBodyControl() {
		return new TenguBodyRotationControl(this);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new JumpTheSkyGoal(this));
		this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new TenguMeleeAttackGoal());
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D) {
			@Override
			public boolean canUse() {
				return !isFallFlying() && super.canUse();
			}
		});
		this.goalSelector.addGoal(8, new FlyingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 28.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
	}

	public void addAdditionalSaveData(CompoundTag p_250330_) {
		super.addAdditionalSaveData(p_250330_);
		p_250330_.putBoolean("IsFallFlying", this.getPose() == Pose.FALL_FLYING);
	}

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
	public void tick() {
		super.tick();
		this.updateFlying();
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
	public void travel(Vec3 p_21280_) {
		if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
			double d0 = 0.08D;
			AttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
			boolean flag = this.getDeltaMovement().y <= 0.0D;
			/*if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
				if (!gravity.hasModifier(SLOW_FALLING)) gravity.addTransientModifier(SLOW_FALLING);
				this.resetFallDistance();
			} else if (gravity.hasModifier(SLOW_FALLING)) {
				gravity.removeModifier(SLOW_FALLING);
			}
			*/
			d0 = Mth.clamp(gravity.getValue() - 0.02F, 0, 0.1);
			if (this.isFallFlying()) {
				this.checkSlowFallDistance();
				Vec3 vec3 = this.getDeltaMovement();
				Vec3 vec31 = this.getLookAngle();
				float f = this.getXRot() * ((float) Math.PI / 180F);
				double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
				double d3 = vec3.horizontalDistance();
				double d4 = vec31.length();
				double d5 = Math.cos((double) f);
				d5 = d5 * d5 * Math.min(1.0D, d4 / 0.4D);
				vec3 = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + d5 * 0.75D), 0.0D);

				if (vec3.y < 0.0D && d1 > 0.0D) {
					double d6 = vec3.y * -0.1D * d5;
					vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
				}

				if (f < 0.0F && d1 > 0.0D) {
					double d10 = d3 * (double) (-Mth.sin(f)) * 0.04D;
					vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2D, -vec31.z * d10 / d1);
				}

				if (d1 > 0.0D) {
					vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1D, 0.0D, (vec31.z / d1 * d3 - vec3.z) * 0.1D);
				}

				this.setDeltaMovement(vec3.multiply((double) 0.99F, (double) 0.98F, (double) 0.99F));
				this.move(MoverType.SELF, this.getDeltaMovement());
				if (this.horizontalCollision && !this.level.isClientSide) {
					double d11 = this.getDeltaMovement().horizontalDistance();
					double d7 = d3 - d11;
					float f1 = (float) (d7 * 10.0D - 3.0D);
					if (f1 > 0.0F) {
						this.playSound(this.getFallDamageSound((int) f1), 1.0F, 1.0F);
						this.hurt(DamageSource.FLY_INTO_WALL, f1);
					}
					this.stopFallFlying();
				}

				if (this.onGround && !this.level.isClientSide) {
					this.setSharedFlag(7, false);
				}
			} else {
				super.travel(p_21280_);
			}
		}

		this.calculateEntityAnimation(this, this instanceof FlyingAnimal);
	}

	private SoundEvent getFallDamageSound(int p_21313_) {
		return p_21313_ > 4 ? this.getFallSounds().big() : this.getFallSounds().small();
	}

	public void updateFlying() {
		if (!this.level.isClientSide) {
			if (this.isEffectiveAi() && this.isFallFlying()) {
				this.navigation = this.flyingNavigation;
			} else {
				this.navigation = this.groundNavigation;
			}
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
			super(Tengu.this, 1.1D, true);
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

				if (this.getTicksUntilNextAttack() == 19) {
					Tengu.this.level.broadcastEntityEvent(Tengu.this, (byte) 4);
				}
			} else {
				this.resetAttackCooldown();
			}

		}
	}

	class TenguMoveControl extends MoveControl {
		private float speed = 0.0F;

		public TenguMoveControl(Mob p_33241_) {
			super(p_33241_);
		}

		public void tick() {
			if (Tengu.this.isFallFlying()) {
				if (this.operation == MoveControl.Operation.MOVE_TO) {
					this.operation = MoveControl.Operation.WAIT;
					double d0 = this.wantedX - Tengu.this.getX();
					double d1 = this.wantedY - Tengu.this.getY();
					double d2 = this.wantedZ - Tengu.this.getZ();
					double d3 = Math.sqrt(d0 * d0 + d2 * d2);
					if (Math.abs(d3) > (double) 1.0E-5F) {
						double d4 = 1.0D - Math.abs(d1 * (double) 0.2F) / d3;
						d0 *= d4;
						d2 *= d4;
						d3 = Math.sqrt(d0 * d0 + d2 * d2);
						double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
						float f = Tengu.this.getYHeadRot();
						float f1 = (float) Mth.atan2(d2, d0);
						float f2 = Mth.wrapDegrees(Tengu.this.getYHeadRot() + 90.0F);
						float f3 = Mth.wrapDegrees(f1 * (180F / (float) Math.PI));
						Tengu.this.setYHeadRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
						if (Mth.degreesDifferenceAbs(f, Tengu.this.getYHeadRot()) < 3.0F) {
							this.speed = Mth.clamp(this.speed + 0.0025F, 0, 0.1F);
						} else {
							this.speed = Mth.clamp(this.speed - 0.005F, 0, 0.1F);
						}

						float f4 = (float) (-(Mth.atan2(-d1, d3) * (double) (180F / (float) Math.PI)));
						Tengu.this.setXRot(f4);
						float f5 = Tengu.this.getYHeadRot() + 90.0F;
						double d6 = (double) (this.speed * this.speedModifier * Mth.cos(f5 * ((float) Math.PI / 180F))) * Math.abs(d0 / d5);
						double d7 = (double) (this.speed * this.speedModifier * Mth.sin(f5 * ((float) Math.PI / 180F))) * Math.abs(d2 / d5);
						double d8 = (double) (this.speed * this.speedModifier * Mth.sin(f4 * ((float) Math.PI / 180F))) * Math.abs(d1 / d5);
						Vec3 vec3 = Tengu.this.getDeltaMovement();
						Tengu.this.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7).scale(0.2D))));
						if (vec3.y <= 0.0025F && d1 > 0.0D && vec3.lengthSqr() >= 0.105D) {
							Tengu.this.setDeltaMovement(vec3.x, 0.25F, vec3.z);
						}
					}
				}
			} else {
				super.tick();
			}
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
