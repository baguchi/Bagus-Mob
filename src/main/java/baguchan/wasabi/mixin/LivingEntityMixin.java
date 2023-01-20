package baguchan.wasabi.mixin;

import baguchan.wasabi.entity.Tengu;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
	private void updateFallFlying(CallbackInfo callbackInfo) {
		LivingEntity livingEntity = (LivingEntity) ((Object) this);
		if(livingEntity instanceof Tengu){
			callbackInfo.cancel();
		}
	}
}
