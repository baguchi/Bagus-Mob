package baguchan.bagusmob.entity.brain;

import bagu_chan.bagus_lib.util.BrainUtils;
import baguchan.bagusmob.entity.BurnerHog;
import baguchan.bagusmob.entity.brain.behaviors.BurnerActive;
import baguchan.bagusmob.entity.brain.behaviors.SetWalkTargetFromAttackTargetIfTargetFar;
import baguchan.bagusmob.registry.ModEntityRegistry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BurnerHogAi {
    public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
    public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
    public static final Item BARTERING_ITEM = Items.GOLD_INGOT;
    private static final int PLAYER_ANGER_RANGE = 16;
    private static final int ANGER_DURATION = 600;
    private static final int ADMIRE_DURATION = 120;
    private static final int MAX_DISTANCE_TO_WALK_TO_ITEM = 9;
    private static final int MAX_TIME_TO_WALK_TO_ITEM = 200;
    private static final int HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM = 200;
    private static final int CELEBRATION_TIME = 300;
    protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
    private static final int BABY_FLEE_DURATION_AFTER_GETTING_HIT = 100;
    private static final int HIT_BY_PLAYER_MEMORY_TIMEOUT = 400;
    private static final int MAX_WALK_DISTANCE_TO_START_RIDING = 8;
    private static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
    private static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static final int MELEE_ATTACK_COOLDOWN = 20;
    private static final int EAT_COOLDOWN = 200;
    private static final int DESIRED_DISTANCE_FROM_ENTITY_WHEN_AVOIDING = 12;
    private static final int MAX_LOOK_DIST = 8;
    private static final int MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM = 14;
    private static final int INTERACTION_RANGE = 8;
    private static final int MIN_DESIRED_DIST_FROM_TARGET_WHEN_HOLDING_CROSSBOW = 5;
    private static final float SPEED_WHEN_STRAFING_BACK_FROM_TARGET = 0.75F;
    private static final int DESIRED_DISTANCE_FROM_ZOMBIFIED = 6;
    private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
    private static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);
    private static final float PROBABILITY_OF_CELEBRATION_DANCE = 0.1F;
    private static final float SPEED_MULTIPLIER_WHEN_AVOIDING = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_MOUNTING = 0.8F;
    private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_CELEBRATE_LOCATION = 1.0F;
    private static final float SPEED_MULTIPLIER_WHEN_DANCING = 0.6F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;

    public static Brain<?> makeBrain(BurnerHog p_34841_, Brain<BurnerHog> p_34842_) {
        initCoreActivity(p_34842_);
        initIdleActivity(p_34842_);
        initFightActivity(p_34841_, p_34842_);
        initCelebrateActivity(p_34842_);
        initRetreatActivity(p_34842_);
        initRideHoglinActivity(p_34842_);
        p_34842_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_34842_.setDefaultActivity(Activity.IDLE);
        p_34842_.useDefaultActivity();
        return p_34842_;
    }

    private static void initCoreActivity(Brain<BurnerHog> p_34821_) {
        p_34821_.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), InteractWithDoor.create(), StopHoldingItemIfNoLongerAdmiring.create(), StartAdmiringItemIfSeen.create(120), StopBeingAngryIfTargetDead.create()));
    }

    private static void initIdleActivity(Brain<BurnerHog> p_34892_) {
        p_34892_.addActivity(Activity.IDLE, 10, ImmutableList.of(StartAttacking.<Piglin>create(AbstractPiglin::isAdult, BurnerHogAi::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), SetLookAndInteract.create(EntityType.PLAYER, 4)));
    }

    private static void initFightActivity(BurnerHog p_34904_, Brain<BurnerHog> p_34905_) {
        p_34905_.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.<BehaviorControl<? super BurnerHog>>of(StopAttackingIfTargetInvalid.<Piglin>create((p_34981_) -> {
            return !isNearestValidAttackTarget(p_34904_, p_34981_);
        }), SetWalkTargetFromAttackTargetIfTargetFar.create(1.0F, 10), SetWalkTargetAwayFrom.entity(MemoryModuleType.ATTACK_TARGET, 1.0F, 6, false), new BurnerActive<>(), RememberIfHoglinWasKilled.create()), MemoryModuleType.ATTACK_TARGET);
    }

    private static void initCelebrateActivity(Brain<BurnerHog> p_34921_) {
    }

    private static void initRetreatActivity(Brain<BurnerHog> p_34959_) {
    }

    private static void initRideHoglinActivity(Brain<BurnerHog> p_34974_) {
    }

    public static void initMemories(BurnerHog p_35095_) {
        GlobalPos globalpos = GlobalPos.of(p_35095_.level().dimension(), p_35095_.blockPosition());
        p_35095_.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    private static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
        return ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 1), Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), 1), Pair.of(SetEntityLookTarget.create(ModEntityRegistry.BURNER_HOG.get(), 8.0F), 1), Pair.of(SetEntityLookTarget.create(8.0F), 1));
    }

    private static RunOne<LivingEntity> createIdleLookBehaviors() {
        return new RunOne<>(ImmutableList.<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>builder().addAll(createLookBehaviors()).add(Pair.of(new DoNothing(30, 60), 1)).build());
    }

    private static RunOne<Piglin> createIdleMovementBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(RandomStroll.stroll(0.6F), 2), Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2), Pair.of(InteractWith.of(ModEntityRegistry.BURNER_HOG.get(), 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2), Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), 2), Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), 2), Pair.of(new DoNothing(30, 60), 1)));
    }


    private static boolean isBabyRidingBaby(Piglin p_34993_) {
        if (!p_34993_.isBaby()) {
            return false;
        } else {
            Entity entity = p_34993_.getVehicle();
            return entity instanceof Piglin && ((Piglin) entity).isBaby() || entity instanceof Hoglin && ((Hoglin) entity).isBaby();
        }
    }


    private static void holdInOffhand(BurnerHog p_34933_, ItemStack p_34934_) {
        if (isHoldingItemInOffHand(p_34933_)) {
            p_34933_.spawnAtLocation(p_34933_.getItemInHand(InteractionHand.OFF_HAND));
        }

        p_34933_.holdInOffHand(p_34934_);
    }

    private static void putInInventory(BurnerHog p_34953_, ItemStack p_34954_) {
        ItemStack itemstack = p_34953_.addToInventory(p_34954_);
        throwItemsTowardRandomPos(p_34953_, Collections.singletonList(itemstack));
    }

    private static void throwItems(Piglin p_34861_, List<ItemStack> p_34862_) {
        Optional<Player> optional = p_34861_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional.isPresent()) {
            throwItemsTowardPlayer(p_34861_, optional.get(), p_34862_);
        } else {
            throwItemsTowardRandomPos(p_34861_, p_34862_);
        }

    }

    private static void throwItemsTowardRandomPos(Piglin p_34913_, List<ItemStack> p_34914_) {
        throwItemsTowardPos(p_34913_, p_34914_, getRandomNearbyPos(p_34913_));
    }

    private static void throwItemsTowardPlayer(Piglin p_34851_, Player p_34852_, List<ItemStack> p_34853_) {
        throwItemsTowardPos(p_34851_, p_34853_, p_34852_.position());
    }

    private static void throwItemsTowardPos(Piglin p_34864_, List<ItemStack> p_34865_, Vec3 p_34866_) {
        if (!p_34865_.isEmpty()) {
            p_34864_.swing(InteractionHand.OFF_HAND);

            for (ItemStack itemstack : p_34865_) {
                BehaviorUtils.throwItem(p_34864_, itemstack, p_34866_.add(0.0D, 1.0D, 0.0D));
            }
        }

    }

    private static boolean wantsToStopRiding(Piglin p_34835_, Entity p_34836_) {
        if (!(p_34836_ instanceof Mob mob)) {
            return false;
        } else {
            return !mob.isBaby() || !mob.isAlive() || wasHurtRecently(p_34835_) || wasHurtRecently(mob) || mob instanceof Piglin && mob.getVehicle() == null;
        }
    }

    private static boolean isNearestValidAttackTarget(Piglin p_34901_, LivingEntity p_34902_) {
        return findNearestValidAttackTarget(p_34901_).filter((p_34887_) -> {
            return p_34887_ == p_34902_;
        }).isPresent();
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Piglin p_35001_) {
        Brain<Piglin> brain = p_35001_.getBrain();
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(p_35001_, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && BrainUtils.isEntityAttackableIgnoringLineOfSight(p_35001_, optional.get(), 24)) {
            return optional;
        } else {
            if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
                Optional<Player> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                if (optional1.isPresent()) {
                    return optional1;
                }
            }

            Optional<Mob> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            if (optional3.isPresent()) {
                return optional3;
            } else {
                Optional<? extends LivingEntity> optional1 = getTargetIfWithinRange(p_35001_, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                return optional1.isPresent() ? optional1 : Optional.empty();
            }
        }
    }

    private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractPiglin p_35092_, MemoryModuleType<? extends LivingEntity> p_35093_) {
        return p_35092_.getBrain().getMemory(p_35093_).filter((p_35108_) -> {
            return p_35108_.closerThan(p_35092_, 24.0D);
        });
    }

    public static Optional<SoundEvent> getSoundForCurrentActivity(Piglin p_34948_) {
        return p_34948_.getBrain().getActiveNonCoreActivity().map((p_34908_) -> {
            return getSoundForActivity(p_34948_, p_34908_);
        });
    }

    private static SoundEvent getSoundForActivity(Piglin p_34855_, Activity p_34856_) {
        if (p_34856_ == Activity.FIGHT) {
            return SoundEvents.PIGLIN_ANGRY;
        } else if (p_34855_.isConverting()) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (p_34856_ == Activity.ADMIRE_ITEM) {
            return SoundEvents.PIGLIN_ADMIRING_ITEM;
        } else if (p_34856_ == Activity.CELEBRATE) {
            return SoundEvents.PIGLIN_CELEBRATE;
        } else {
            return isNearRepellent(p_34855_) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
        }
    }

    protected static List<AbstractPiglin> getVisibleAdultPiglins(Piglin p_35005_) {
        return p_35005_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static void stopWalking(Piglin p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }

    private static BehaviorControl<LivingEntity> babySometimesRideBabyHoglin() {
        SetEntityLookTargetSometimes.Ticker setentitylooktargetsometimes$ticker = new SetEntityLookTargetSometimes.Ticker(RIDE_START_INTERVAL);
        return CopyMemoryWithExpiry.create((p_289472_) -> {
            return p_289472_.isBaby() && setentitylooktargetsometimes$ticker.tickDownAndCheck(p_289472_.level().random);
        }, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION);
    }

    public static Optional<LivingEntity> getAvoidTarget(Piglin p_34987_) {
        return p_34987_.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? p_34987_.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
    }

    public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin p_34894_) {
        return p_34894_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) ? p_34894_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) : Optional.empty();
    }

    private static void broadcastRetreat(Piglin p_34930_, LivingEntity p_34931_) {
        getVisibleAdultPiglins(p_34930_).stream().filter((p_34985_) -> {
            return p_34985_ instanceof Piglin;
        }).forEach((p_34819_) -> {
            retreatFromNearestTarget((Piglin) p_34819_, p_34931_);
        });
    }

    private static void retreatFromNearestTarget(Piglin p_34950_, LivingEntity p_34951_) {
        Brain<Piglin> brain = p_34950_.getBrain();
        LivingEntity $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.AVOID_TARGET), p_34951_);
        $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.ATTACK_TARGET), $$3);
        setAvoidTargetAndDontHuntForAWhile(p_34950_, $$3);
    }

    private static void setAvoidTargetAndDontHuntForAWhile(Piglin p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_34968_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34969_, (long) RETREAT_DURATION.sample(p_34968_.level().random));
        dontKillAnyMoreHoglinsForAWhile(p_34968_);
    }

    protected static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin p_34923_) {
        p_34923_.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, (long) TIME_BETWEEN_HUNTS.sample(p_34923_.level().random));
    }

    private static void eat(Piglin p_35015_) {
        p_35015_.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, 200L);
    }

    private static Vec3 getRandomNearbyPos(Piglin p_35017_) {
        Vec3 vec3 = LandRandomPos.getPos(p_35017_, 4, 2);
        return vec3 == null ? p_35017_.position() : vec3;
    }

    private static boolean hasEatenRecently(Piglin p_35019_) {
        return p_35019_.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
    }

    protected static boolean isIdle(AbstractPiglin p_34943_) {
        return p_34943_.getBrain().isActive(Activity.IDLE);
    }

    private static boolean hasCrossbow(LivingEntity p_34919_) {
        return p_34919_.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem);
    }

    private static void admireGoldItem(LivingEntity p_34939_) {
        p_34939_.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static boolean isAdmiringItem(Piglin p_35021_) {
        return p_35021_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    private static boolean isBarterCurrency(ItemStack p_149968_) {
        return p_149968_.is(BARTERING_ITEM);
    }

    private static boolean isFood(ItemStack p_149970_) {
        return p_149970_.is(ItemTags.PIGLIN_FOOD);
    }

    private static boolean isNearRepellent(Piglin p_35023_) {
        return p_35023_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }


    private static boolean isAdmiringDisabled(Piglin p_35025_) {
        return p_35025_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean wasHurtRecently(LivingEntity p_34989_) {
        return p_34989_.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }

    private static boolean isHoldingItemInOffHand(Piglin p_35027_) {
        return !p_35027_.getOffhandItem().isEmpty();
    }


    public static boolean isZombified(EntityType<?> p_34807_) {
        return p_34807_ == EntityType.ZOMBIFIED_PIGLIN || p_34807_ == EntityType.ZOGLIN;
    }
}
