package baguchan.bagusmob.entity.projectile;

import baguchan.bagusmob.registry.ModDamageSource;
import baguchan.bagusmob.registry.ModEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SoulProjectile extends ThrowableProjectile {
    public float damage = 3;

    public SoulProjectile(EntityType<? extends SoulProjectile> p_i50154_1_, Level p_i50154_2_) {
        super(p_i50154_1_, p_i50154_2_);
    }

    public SoulProjectile(Level worldIn, LivingEntity throwerIn) {
        super(ModEntityRegistry.SOUL.get(), throwerIn, worldIn);
    }

    public SoulProjectile(Level worldIn, double x, double y, double z) {
        super(ModEntityRegistry.SOUL.get(), x, y, z, worldIn);

    }

    public SoulProjectile(EntityType<? extends SoulProjectile> p_i50154_1_, Level worldIn, double x, double y, double z) {
        super(p_i50154_1_, x, y, z, worldIn);
    }

    public SoulProjectile(EntityType<? extends SoulProjectile> entityType, LivingEntity throwerIn, Level worldIn) {
        super(entityType, throwerIn, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            this.level().addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 0D, 0D, 0D);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.tickCount >= 60) {
                this.discard();
            }

            if (this.tickCount % 2 == 0) {
                this.level().broadcastEntityEvent(this, (byte) 3);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);

        Entity entity = p_37259_.getEntity();
        if (!entity.hurt(this.damageSources().source(ModDamageSource.SOUL, this, this.getOwner()), this.damage)) {
            if (!this.level().isClientSide) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    public void addAdditionalSaveData(CompoundTag p_37222_) {
        super.addAdditionalSaveData(p_37222_);
        p_37222_.putFloat("Damage", (byte) this.damage);
    }

    public void readAdditionalSaveData(CompoundTag p_37220_) {
        super.readAdditionalSaveData(p_37220_);
        if (p_37220_.contains("Damage", 99)) {
            this.damage = p_37220_.getFloat("Damage");
        }
    }
}