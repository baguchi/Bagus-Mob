package baguchan.bagusmob.entity;

import bagu_chan.bagus_lib.entity.goal.AnimateAttackGoal;
import baguchan.bagusmob.entity.goal.GuardWithSwordGoal;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
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
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class Samurai extends AbstractIllager {
    public final int attackAnimationLength = (int) (20 * 1.125F);
    public final int attackAnimationActionPoint = (int) (int) (0.5F * 20);
    private int attackAnimationTick;
    public final AnimationState attackAnimationState = new AnimationState();

    public Samurai(EntityType<? extends Samurai> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GuardWithSwordGoal(this));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(4, new AnimateAttackGoal(this, 0.95F, attackAnimationActionPoint, attackAnimationLength));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers(AbstractIllager.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.24F).add(Attributes.MAX_HEALTH, 32.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide()) {
            if (this.attackAnimationTick < this.attackAnimationLength) {
                this.attackAnimationTick++;
            }

            if (this.attackAnimationTick >= this.attackAnimationLength) {
                this.attackAnimationState.stop();
            }
        }
    }

    @Override
    public boolean isBlocking() {
        return super.isBlocking();
    }

    @Override
    public void handleEntityEvent(byte p_219360_) {
        if (p_219360_ == 4) {
            this.attackAnimationTick = 0;
            this.attackAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(p_219360_);
        }
    }


    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_
    ) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        this.populateDefaultEquipmentEnchantments(randomsource, p_34089_);
        return spawngroupdata;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_219149_, DifficultyInstance p_219150_) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItemRegistry.KATANA.get()));
        }
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItemRegistry.KATANA.get()));
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

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(0.25F, 0, 0.25F);
    }

    @Override
    public boolean isAlliedTo(Entity p_34110_) {
        if (super.isAlliedTo(p_34110_)) {
            return true;
        } else if (p_34110_ instanceof LivingEntity && ((LivingEntity) p_34110_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_34110_.getTeam() == null;
        } else {
            return false;
        }
    }
}
