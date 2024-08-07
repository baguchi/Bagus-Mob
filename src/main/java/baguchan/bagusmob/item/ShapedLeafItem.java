package baguchan.bagusmob.item;

import baguchan.bagusmob.entity.projectile.SlashAir;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;


public class ShapedLeafItem extends Item {
	public ShapedLeafItem(Item.Properties properties) {
		super(properties);
	}

	public static ItemAttributeModifiers createAttributes() {
		return ItemAttributeModifiers.builder()
				.add(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(
								BASE_ATTACK_DAMAGE_ID, 3.0F, AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.MAINHAND
				)
				.add(
						Attributes.ATTACK_SPEED,
						new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4F, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.build();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (!levelIn.isClientSide) {
			SlashAir slashAir = new SlashAir(levelIn, playerIn);
			slashAir.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 0.8F);
			levelIn.addFreshEntity(slashAir);

		}
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		playerIn.getCooldowns().addCooldown(itemstack.getItem(), 40);
		if (!playerIn.level().isClientSide) {
			itemstack.hurtAndBreak(1, playerIn, LivingEntity.getSlotForHand(handIn));
		}
		return InteractionResultHolder.sidedSuccess(itemstack, levelIn.isClientSide());
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return 18;
	}

	public boolean isValidRepairItem(ItemStack p_43311_, ItemStack p_43312_) {
		return super.isValidRepairItem(p_43311_, p_43312_);
	}

	public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
		return !p_43294_.isCreative();
	}

	public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {

		return true;
	}

	@Override
	public void postHurtEnemy(ItemStack p_345553_, LivingEntity p_345771_, LivingEntity p_346282_) {
		p_345553_.hurtAndBreak(1, p_346282_, EquipmentSlot.MAINHAND);
	}

	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
		return itemAbility == ItemAbilities.SWORD_SWEEP;
	}
}
