package baguchan.bagusmob.mixin;

import baguchan.bagusmob.entity.Tengu;
import baguchan.bagusmob.registry.ModEffects;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private int lockTickCount = 0;

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect p_21024_);

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(MobEffect p_21125_);

    @Shadow
    protected abstract void tickEffects();

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot p_21127_);

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
	private void updateFallFlying(CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) ((Object) this);
		if(livingEntity instanceof Tengu){
			callbackInfo.cancel();
		}
	}

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo callbackInfo) {
        if (hasEffect(ModEffects.TIME_LOCK.get())) {
            int pingTick = 15;
            int lockDuration = getEffect(ModEffects.TIME_LOCK.get()).getDuration();

            if (lockDuration <= 28) {
                pingTick = 4;
            } else if (lockDuration <= 28 + 8 * 9) {
                pingTick = 8;
            }
            if (this.lockTickCount % pingTick == 0) {
                this.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.5F, 1.0F);
            }
            this.tickEffects();
            this.lockTickCount += 1;
            callbackInfo.cancel();
        } else {
            this.lockTickCount = 0;
        }
    }

    @Override
    public boolean isSteppingCarefully() {
        return this.getItemBySlot(EquipmentSlot.FEET).is(ModItemRegistry.NINJA_BOOTS.get()) || super.isSteppingCarefully();
    }
}
