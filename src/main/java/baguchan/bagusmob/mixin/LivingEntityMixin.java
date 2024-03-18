package baguchan.bagusmob.mixin;

import baguchan.bagusmob.entity.Tengu;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.sounds.SoundEvents;
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

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }


    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot p_21127_);

    @Shadow
    public abstract ItemStack getUseItem();

    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
	private void updateFallFlying(CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) ((Object) this);
		if(livingEntity instanceof Tengu){
			callbackInfo.cancel();
		}
	}

    @Inject(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", ordinal = 1, shift = At.Shift.BEFORE), cancellable = true)
    public void hurt(byte p_20975_, CallbackInfo ci) {
        if (this.getUseItem().is(ModItemRegistry.KATANA.get())) {
            this.playSound(SoundEvents.ANVIL_PLACE, 1.0F, 1.25F + this.random.nextFloat() * 0.25F);
            ci.cancel();
        }
    }

    @Override
    public boolean isInvisible() {
        return super.isInvisible() || this.isSteppingCarefully() && this.getItemBySlot(EquipmentSlot.CHEST).is(ModItemRegistry.NINJA_CHESTPLATE.get()) && this.getItemBySlot(EquipmentSlot.HEAD).is(ModItemRegistry.NINJA_HOOD.get());
    }
}
