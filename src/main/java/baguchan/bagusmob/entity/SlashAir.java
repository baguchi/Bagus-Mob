package baguchan.bagusmob.entity;

import baguchan.bagusmob.registry.ModEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SlashAir extends ThrowableProjectile {
	public float damage = 2;

	public SlashAir(EntityType<? extends SlashAir> p_i50154_1_, Level p_i50154_2_) {
		super(p_i50154_1_, p_i50154_2_);
	}

	public SlashAir(Level worldIn, LivingEntity throwerIn) {
		super(ModEntityRegistry.SLASH_AIR.get(), throwerIn, worldIn);
	}

	public SlashAir(Level worldIn, double x, double y, double z) {
		super(ModEntityRegistry.SLASH_AIR.get(), x, y, z, worldIn);

	}

	public SlashAir(EntityType<? extends SlashAir> p_i50154_1_, Level worldIn, double x, double y, double z) {
		super(p_i50154_1_, x, y, z, worldIn);
	}

	public SlashAir(EntityType<? extends SlashAir> entityType, LivingEntity throwerIn, Level worldIn) {
		super(entityType, throwerIn, worldIn);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			this.level().addAlwaysVisibleParticle(ParticleTypes.SWEEP_ATTACK, getX(), getY(), getZ(), 0D, 0D, 0D);
		}
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide) {
			if (this.tickCount >= 60) {
				this.discard();
			}

			if (this.tickCount % 3 == 0) {
				this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);
				this.level().broadcastEntityEvent(this, (byte) 3);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult p_37259_) {
		super.onHitEntity(p_37259_);

		Entity entity = p_37259_.getEntity();
		double d0 = entity.getX() - this.getX();
		double d1 = entity.getZ() - this.getZ();
		double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
		entity.push(d0 / d2 * 2D, 0.2D, d1 / d2 * 2D);
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