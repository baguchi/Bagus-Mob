package baguchan.bagusmob.item;

import baguchan.bagusmob.entity.projectile.SpinBlade;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

/*
 * Thanks Twilight Forest team for Block and Chain Code!
 * https://github.com/TeamTwilight/twilightforest/blob/1.20.x/src/main/java/twilightforest/item/ChainBlockItem.java#L26
 */
public class SpinBladeItem extends Item {

    private static final String THROWN_UUID_KEY = "spin_blade_uuid";

    public SpinBladeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean isSelected) {
        if (!level.isClientSide() && getThrownUuid(stack) != null && this.getThrownEntity(level, stack) == null) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, compoundTag -> {
                compoundTag.remove(THROWN_UUID_KEY);
            });
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (getThrownUuid(stack) != null)
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);

        player.playSound(SoundEvents.ARROW_SHOOT, 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));

        if (!level.isClientSide()) {
            SpinBlade spinBlade = new SpinBlade(level, player, stack);
            spinBlade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.0F);

            level.addFreshEntity(spinBlade);
            this.setThrownEntity(stack, spinBlade);
        }

        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Nullable
    public static UUID getThrownUuid(ItemStack stack) {
        if (!stack.get(DataComponents.CUSTOM_DATA).isEmpty()) {
            return stack.get(DataComponents.CUSTOM_DATA).copyTag().getUUID(THROWN_UUID_KEY);
        }

        return null;
    }

    @Nullable
    private SpinBlade getThrownEntity(Level level, ItemStack stack) {
        if (level instanceof ServerLevel server) {
            UUID id = getThrownUuid(stack);
            if (id != null) {
                Entity e = server.getEntity(id);
                if (e instanceof SpinBlade) {
                    return (SpinBlade) e;
                }
            }
        }

        return null;
    }

    private void setThrownEntity(ItemStack stack, SpinBlade cube) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, compoundTag -> {
            compoundTag.putUUID(THROWN_UUID_KEY, cube.getUUID());
        });
    }

    @Override
    public int getUseDuration(ItemStack p_41454_, LivingEntity p_344979_) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }
}
