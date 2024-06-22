package baguchan.bagusmob.item;

import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

public class DaggerItem extends Item {
	public DaggerItem(Properties properties) {
		super(properties);
	}

	public static ItemAttributeModifiers createAttributes() {
		return ItemAttributeModifiers.builder()
				.add(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(
								BASE_ATTACK_DAMAGE_ID, 5.0F, AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.MAINHAND
				)
				.add(
						Attributes.ATTACK_SPEED,
						new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.2F, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.build();
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return 22;
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

	@Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return toolAction == ToolActions.SWORD_DIG;
	}
}
