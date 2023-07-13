package baguchan.bagusmob.entity;

import baguchan.bagusmob.entity.goal.ConstructGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Modifiger extends AbstractIllager {
    private static final String[] STRUCTURE_LOCATIONS = new String[]{"pillager_outpost/watchtower"};


    private float walkScale;
    private Optional<BlockPos> buildingPos = Optional.empty();
    private ResourceLocation buildingStructureName;
    private int buildingStep;

    public Modifiger(EntityType<? extends Modifiger> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RaiderOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new HoldGroundAttackGoal(this, 10.0F));

        this.goalSelector.addGoal(6, new ConstructGoal(this, STRUCTURE_LOCATIONS, 0.8F));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers(AbstractIllager.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));

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

    public Optional<BlockPos> getBuildingPos() {
        return this.buildingPos;
    }

    public void setBuildingPos(Optional<BlockPos> buildingPos) {
        this.buildingPos = buildingPos;
    }

    public int getBuildingStep() {
        return buildingStep;
    }

    public void setBuildingStep(int buildingStep) {
        this.buildingStep = buildingStep;
    }

    public ResourceLocation getBuildingStructureName() {
        return buildingStructureName;
    }

    public void setBuildingStructureName(ResourceLocation buildingStructureName) {
        this.buildingStructureName = buildingStructureName;
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
        if (this.level().isClientSide()) {
            if ((this.walkAnimation.isMoving())) {
                walkScale = Mth.clamp(walkScale + 0.1F, 0, 1);

            } else {

                walkScale = Mth.clamp(walkScale - 0.1F, 0, 1);
            }
        }
        super.tick();
    }

    @Override
    public IllagerArmPose getArmPose() {
        return this.isCelebrating() ? IllagerArmPose.CELEBRATING : IllagerArmPose.CROSSED;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_37870_) {
        super.addAdditionalSaveData(p_37870_);
        if (this.getBuildingPos().isPresent()) {
            p_37870_.put("BuildingPos", NbtUtils.writeBlockPos(this.getBuildingPos().get()));
        }

        if (this.buildingStructureName != null) {
            p_37870_.putString("BuildingName", this.buildingStructureName.toString());
        }
        p_37870_.putInt("BuildingStep", this.buildingStep);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_37862_) {
        super.readAdditionalSaveData(p_37862_);
        if (p_37862_.contains("BuildingPos", 10)) {
            this.setBuildingPos(Optional.of(NbtUtils.readBlockPos(p_37862_.getCompound("BuildingPos"))));
        }
        if (p_37862_.contains("BuildingName")) {
            this.setBuildingStructureName(ResourceLocation.tryParse(p_37862_.getString("BuildingName")));
        }
        if (p_37862_.contains("BuildingStep")) {
            this.setBuildingStep(p_37862_.getInt("BuildingStep"));
        }
    }

    @Override
    public boolean canBeLeader() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public float getWalkScale() {
        return walkScale;
    }
}
