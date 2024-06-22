package baguchan.bagusmob.entity;

import bagu_chan.bagus_lib.entity.AnimationScale;
import baguchan.bagusmob.entity.brain.BurnerHogAi;
import baguchan.bagusmob.entity.brain.RudeHogAi;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BurnerHog extends Piglin {
    private static final EntityDataAccessor<Boolean> DATA_CHARGE = SynchedEntityData.defineId(BurnerHog.class, EntityDataSerializers.BOOLEAN);

    protected static final ImmutableList<SensorType<? extends Sensor<? super BurnerHog>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.HOME);

    public final AnimationScale movingScale = new AnimationScale();

    public BurnerHog(EntityType<? extends BurnerHog> p_i48556_1_, Level p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
        this.xpReward = 30;
        this.setCanPickUpLoot(false);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHARGE, false);
    }

    public void setCharge(boolean charge) {
        this.entityData.set(DATA_CHARGE, charge);
    }

    public boolean isCharge() {
        return this.entityData.get(DATA_CHARGE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.MOVEMENT_SPEED, (double) 0.35D).add(Attributes.ARMOR, 10.0D).add(Attributes.ARMOR_TOUGHNESS, 1F).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            movingScale.tick(this);
            movingScale.setFlag(this.walkAnimation.isMoving());

        }
        super.tick();
    }

    protected Brain.Provider<BurnerHog> revampedBrainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }


    @Override
    protected Brain<?> makeBrain(Dynamic<?> p_34723_) {
        return BurnerHogAi.makeBrain(this, this.revampedBrainProvider().makeBrain(p_34723_));
    }

    @Override
    public InteractionResult mobInteract(Player p_34745_, InteractionHand p_34746_) {
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34717_, DifficultyInstance p_34718_, MobSpawnType p_34719_, @Nullable SpawnGroupData p_34720_) {
        BurnerHogAi.initMemories(this);

        return super.finalizeSpawn(p_34717_, p_34718_, p_34719_, p_34720_);
    }

    public void holdInMainHand(ItemStack p_34784_) {
        super.holdInMainHand(p_34784_);
    }

    @Override
    public boolean canHunt() {
        return super.canHunt();
    }

    public void holdInOffHand(ItemStack p_34786_) {
        super.holdInOffHand(p_34786_);
    }

    public ItemStack addToInventory(ItemStack p_34779_) {
        return super.addToInventory(p_34779_);
    }

    public boolean canAddToInventory(ItemStack p_34781_) {
        return super.canAddToInventory(p_34781_);
    }

    public boolean canReplaceCurrentItem(ItemStack p_34788_) {
        EquipmentSlot equipmentslot = this.getEquipmentSlotForItem(p_34788_);
        ItemStack itemstack = this.getItemBySlot(equipmentslot);
        return this.canReplaceCurrentItem(p_34788_, itemstack);
    }

    public boolean canReplaceCurrentItem(ItemStack p_34712_, ItemStack p_34713_) {
        if (EnchantmentHelper.has(p_34713_, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
            return false;
        } else {
            boolean flag = RudeHogAi.isLovedItem(p_34712_) || p_34712_.is(Items.CROSSBOW);
            boolean flag1 = RudeHogAi.isLovedItem(p_34713_) || p_34713_.is(Items.CROSSBOW);
            if (flag && !flag1) {
                return true;
            } else if (!flag && flag1) {
                return false;
            } else {
                return this.isAdult() && !p_34712_.is(Items.CROSSBOW) && p_34713_.is(Items.CROSSBOW) ? false : super.canReplaceCurrentItem(p_34712_, p_34713_);
            }
        }
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_BRUTE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_35072_) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    protected void playStepSound(BlockPos p_35066_, BlockState p_35067_) {
        this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
    }

    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, 1.0F, this.getVoicePitch());
    }
}
