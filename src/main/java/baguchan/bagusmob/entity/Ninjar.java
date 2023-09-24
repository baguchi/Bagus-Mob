package baguchan.bagusmob.entity;

import bagu_chan.bagus_lib.entity.goal.AnimatedAttackGoal;
import baguchan.bagusmob.entity.goal.AppearGoal;
import baguchan.bagusmob.entity.goal.DisappearGoal;
import baguchan.bagusmob.registry.ModItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

public class Ninjar extends AbstractIllager {

	private float runningScale;
	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState slashRightAnimationState = new AnimationState();
	public final AnimationState slashLeftAnimationState = new AnimationState();
	public final AnimationState appearAnimationState = new AnimationState();
	public final AnimationState disappearAnimationState = new AnimationState();

    public int attackAnimationTick;
    private final int attackAnimationLength = (int) (20 * 0.4F);
    private final int attackAnimationLeftActionPoint = (int) ((int) attackAnimationLength - (3));
	public Ninjar(EntityType<? extends Ninjar> p_32105_, Level p_32106_) {
		super(p_32105_, p_32106_);
		this.xpReward = 10;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_) {
		if (DATA_POSE.equals(p_219422_)) {
			switch (this.getPose()) {
				case EMERGING:
					this.disappearAnimationState.stop();
					this.appearAnimationState.start(this.tickCount);
					break;
				case DIGGING:
					this.appearAnimationState.stop();
					this.disappearAnimationState.start(this.tickCount);
					break;
				default:
					this.disappearAnimationState.stop();
					this.appearAnimationState.stop();
			}
		}

		super.onSyncedDataUpdated(p_219422_);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new AppearGoal(this, 20));
		this.goalSelector.addGoal(0, new DisappearGoal(this, 20));
		this.goalSelector.addGoal(2, new RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(4, new AnimatedAttackGoal(this, 1.2D, attackAnimationLeftActionPoint, attackAnimationLength));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers(AbstractIllager.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}

	@Override
	protected PathNavigation createNavigation(Level p_218342_) {
		GroundPathNavigation path = new GroundPathNavigation(this, p_218342_);
		path.setCanOpenDoors(true);
		path.setCanFloat(true);
		path.setCanPassDoors(true);
		return path;
	}

	public boolean teleport() {
		if (!this.level().isClientSide() && this.isAlive()) {
			double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 40.0D;
			double d1 = this.getY() + (double) (this.random.nextInt(40) - 20);
			double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 40.0D;
			return this.teleport(d0, d1, d2);
		} else {
			return false;
		}
	}

	public boolean teleport(double p_32544_, double p_32545_, double p_32546_) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_32544_, p_32545_, p_32546_);

		while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
			blockpos$mutableblockpos.move(Direction.DOWN);
		}

		BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
		boolean flag = blockstate.blocksMotion();
		boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
		if (flag && !flag1) {
			net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, p_32544_, p_32545_, p_32546_);
			if (event.isCanceled()) return false;
			Vec3 vec3 = this.position();
			boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), false);
			if (flag2) {
				this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
				if (!this.isSilent()) {
					this.level().playSound((Player) null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
					this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
				}
			}

			return flag2;
		} else {
			return false;
		}
	}

	@Override
	protected float getSoundVolume() {
		return this.isInvisible() ? 0.35F : 1.0F;
	}


	@Override
	public void aiStep() {
		super.aiStep();

		if (!this.level().isClientSide) {
			if (this.isInvisible() && (this.getTarget() != null || this.isCelebrating())) {
				this.setInvisible(false);
			} else if (!this.isInvisible() && (this.getTarget() == null && !this.isCelebrating())) {
				this.setInvisible(true);
			}
		}
	}

	public boolean isSteppingCarefully() {
		return true;
	}

	@Override
	public boolean dampensVibrations() {
		return this.isInvisible();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 26.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 8.0D).add(Attributes.FOLLOW_RANGE, 22.0D);
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
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide) {
            if (this.attackAnimationTick < this.attackAnimationLength) {
                this.attackAnimationTick++;
            }

            if (this.attackAnimationTick >= this.attackAnimationLength) {
                this.slashLeftAnimationState.stop();
                this.slashRightAnimationState.stop();
            }
        }
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if (p_21375_ == 4) {
            if (this.isLeftHanded()) {
                this.slashLeftAnimationState.start(this.tickCount);
            } else {
                this.slashRightAnimationState.start(this.tickCount);
            }
            this.attackAnimationTick = 0;
        } else {
            super.handleEntityEvent(p_21375_);
        }
    }

	@Override
	public void tick() {
		if (this.level().isClientSide()) {
			if ((this.isMoving())) {
				if (isDashing()) {
					idleAnimationState.stop();
					runningScale = Mth.clamp(runningScale + 0.1F, 0, 1);
				} else {
					idleAnimationState.stop();
					runningScale = Mth.clamp(runningScale - 0.1F, 0, 1);
				}
			} else {
				idleAnimationState.startIfStopped(this.tickCount);
			}
		}
		super.tick();
	}


	private boolean isDashing() {
		return this.getDeltaMovement().horizontalDistanceSqr() > 0.02D;
	}

	private boolean isMoving() {
		return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
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
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItemRegistry.DAGGER.get()));
		}
	}

	@Override
	public void applyRaidBuffs(int p_37844_, boolean p_37845_) {
		ItemStack itemstack = new ItemStack(ModItemRegistry.DAGGER.get());
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
	public IllagerArmPose getArmPose() {
		return this.isCelebrating() ? IllagerArmPose.CELEBRATING : IllagerArmPose.CROSSED;
	}

	public void addAdditionalSaveData(CompoundTag p_250330_) {
		super.addAdditionalSaveData(p_250330_);
		p_250330_.putBoolean("IsDisappear", this.getPose() == Pose.DIGGING);
		p_250330_.putBoolean("IsAppear", this.getPose() == Pose.EMERGING);
	}

	public void readAdditionalSaveData(CompoundTag p_250781_) {
		super.readAdditionalSaveData(p_250781_);
		if (p_250781_.getBoolean("IsDisappear")) {
			this.setPose(Pose.DIGGING);
		}
		if (p_250781_.getBoolean("IsAppear")) {
			this.setPose(Pose.EMERGING);
		}
	}

	@Override
	public boolean hurt(DamageSource p_37849_, float p_37850_) {

		if (this.getPose() == Pose.DIGGING || this.getPose() == Pose.EMERGING) {
			if (!p_37849_.is(DamageTypeTags.IS_EXPLOSION)) {
				return super.hurt(p_37849_, p_37850_ * 0.25F);
			} else {
				return false;
			}
		}
		if (!this.level().isClientSide() && (p_37849_.getEntity() instanceof LivingEntity) && this.random.nextInt(4) == 0) {
			//play Disappear
			this.setPose(Pose.DIGGING);
		}
		return super.hurt(p_37849_, p_37850_);
	}

	@Override
	public boolean canBeLeader() {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public float getRunningScale() {
		return runningScale;
	}

	class NinjarMeleeAttackGoal extends MeleeAttackGoal {
		public NinjarMeleeAttackGoal() {
			super(Ninjar.this, 1.2D, true);
		}

		protected void checkAndPerformAttack(LivingEntity p_29589_, double p_29590_) {
			double d0 = this.getAttackReachSqr(p_29589_);
			if (p_29590_ <= d0 && this.getTicksUntilNextAttack() <= 16) {
				this.resetAttackCooldown();
				this.mob.doHurtTarget(p_29589_);
			} else if (p_29590_ <= d0) {
				if (this.isTimeToAttack()) {
					this.resetAttackCooldown();
				}

				if (this.getTicksUntilNextAttack() == 19) {
					Ninjar.this.level().broadcastEntityEvent(Ninjar.this, (byte) 4);
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
			if (Ninjar.this.isFallFlying()) {
				Ninjar.this.setYRot(Ninjar.this.getYHeadRot());
				Ninjar.this.setYBodyRot(Ninjar.this.getYHeadRot());
			} else {
				super.clientTick();
			}
		}
	}
}
