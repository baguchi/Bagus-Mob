package baguchan.bagusmob.entity;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PotSnake extends Monster {
    public static final EntityDimensions POT_SNAKE_DIMENSIONS = EntityDimensions.fixed(0.95F, 1.0F);

    private static final ResourceLocation COVERED_ARMOR_MODIFIER_UUID = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "pot_snake");
    private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(
            COVERED_ARMOR_MODIFIER_UUID, 10.0, AttributeModifier.Operation.ADD_VALUE
    );
    private static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(PotSnake.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> POT = SynchedEntityData.defineId(PotSnake.class, EntityDataSerializers.ITEM_STACK);

    public final AnimationState standingAnimationState = new AnimationState();
    public final AnimationState agressiveAnimationState = new AnimationState();

    public int standingAnimationTick;
    public final int standingAnimationLength = (int) (20F * 0.75F);

    public PotSnake(EntityType<? extends PotSnake> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(POT, ItemStack.EMPTY);
        builder.define(HIDING, true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1D, true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, PotSnake.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.95D) {
            @Override
            public boolean canUse() {
                return !isHiding() && super.canUse();
            }
        });
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.26F).add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected InteractionResult mobInteract(Player p_21472_, InteractionHand p_21473_) {
        ItemStack stack = p_21472_.getItemInHand(p_21473_);
        if (stack.is(Blocks.DECORATED_POT.asItem())) {
            setPot(stack.copy());
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(p_21472_, p_21473_);
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_21544_) {
        if (p_21544_ != this.getTarget() && p_21544_ != null) {
            this.setHiding(false);
        }
        super.setTarget(p_21544_);

    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_146754_) {

        if (p_146754_.equals(HIDING)) {
            if (!this.isHiding() && this.isHasPot()) {
                this.standingAnimationTick = 0;
                this.standingAnimationState.startIfStopped(this.tickCount);
            }
        }
        if (p_146754_.equals(POT)) {
            if (!this.isHasPot()) {
                if (this.level().isClientSide) {
                    this.spawnBreakingParticle(8);
                }
            }
        }
        super.onSyncedDataUpdated(p_146754_);
        if (p_146754_.equals(POT)) {
            this.refreshDimensions();
        }
    }

    @Override
    public void setDeltaMovement(Vec3 p_20257_) {
        if (!this.isHiding()) {
            super.setDeltaMovement(p_20257_);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isHiding() && this.isHasPot();
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().deflate(0.4F, 0.25F, 0.4F);
    }

    protected void spawnBreakingParticle(int count) {
        BlockState blockstate = Blocks.TERRACOTTA.defaultBlockState();
        for (int i = 0; i < count; i++) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5);
            double d1 = this.getY() + (this.random.nextDouble());
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5);
            this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0F, 0F, 0F);
        }
    }

    @Override
    public Vec3 getDeltaMovement() {
        return this.isHiding() ? Vec3.ZERO : super.getDeltaMovement();
    }

    public void setPot(ItemStack p_34565_) {
        this.entityData.set(POT, p_34565_);
        if (!this.level().isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER);
            if (!p_34565_.isEmpty()) {
                this.getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
            } else {
                this.playSound(SoundEvents.DECORATED_POT_SHATTER, 1.0F, 1.0F);
                this.gameEvent(GameEvent.BLOCK_DESTROY);
            }
        }
    }

    public boolean isHasPot() {
        return !this.entityData.get(POT).isEmpty();
    }

    public ItemStack getPot() {
        return this.entityData.get(POT);
    }

    public void setHiding(boolean p_34565_) {
        this.entityData.set(HIDING, p_34565_);
    }

    public boolean isHiding() {
        return this.entityData.get(HIDING);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        if (!this.getPot().isEmpty()) {
            p_21484_.put("Pot", this.getPot().save(this.registryAccess()));
        }
        p_21484_.putBoolean("Hiding", this.isHiding());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setPot(ItemStack.parse(this.registryAccess(), p_21450_.getCompound("Pot")).orElse(ItemStack.EMPTY));
        this.setHiding(p_21450_.getBoolean("Hiding"));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide) {
            if (this.standingAnimationTick < this.standingAnimationLength) {
                this.standingAnimationTick++;
            }

            if (this.standingAnimationTick >= this.standingAnimationLength) {
                this.standingAnimationState.stop();
            }
        }

        if (this.isHiding()) {
            this.setRot(0, 0);
            this.xRotO = 0F;
            this.yRotO = 0F;
            this.yBodyRot = 0F;
        }
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        boolean flag = super.hurt(p_21016_, p_21017_);
        if (flag && this.isHasPot() && p_21016_.is(DamageTypeTags.IS_PROJECTILE)) {
            for (Item item : this.getPot().getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY).ordered()) {
                spawnAtLocation(item);
            }
            this.setHiding(false);
            this.setPot(ItemStack.EMPTY);
        }
        return flag;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel p_348683_, DamageSource p_21385_, boolean p_21387_) {
        super.dropCustomDeathLoot(p_348683_, p_21385_, p_21387_);
        if (isHasPot()) {
            for (Item item : this.getPot().getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY).ordered()) {
                spawnAtLocation(item);
            }
            this.setHiding(false);
            this.setPot(ItemStack.EMPTY);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_, MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_) {
        ItemStack stack = Items.DECORATED_POT.getDefaultInstance();
        PotDecorations decoratedpotblockentity$decorations = new PotDecorations(ModItemRegistry.SNAKE_POTTERY_SHERD.get(), ModItemRegistry.SNAKE_POTTERY_SHERD.get(), ModItemRegistry.SNAKE_POTTERY_SHERD.get(), ModItemRegistry.SNAKE_POTTERY_SHERD.get());
        if (random.nextBoolean()) {
            stack = DecoratedPotBlockEntity.createDecoratedPotItem(decoratedpotblockentity$decorations);
        }
        if (this.getPot().isEmpty()) {
            this.setPot(stack);
        }
        this.setHiding(true);

        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_);
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose p_316700_) {
        return this.isHasPot() ? POT_SNAKE_DIMENSIONS.scale(this.getScale()) : super.getDefaultDimensions(p_316700_);
    }
}
