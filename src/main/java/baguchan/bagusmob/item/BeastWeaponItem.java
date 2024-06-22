package baguchan.bagusmob.item;

import baguchan.bagusmob.BagusMob;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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

public class BeastWeaponItem extends Item {
    protected static final ResourceLocation BASE_ATTACK_KNOCKBACK_ID = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "knockback");
    protected static final ResourceLocation BASE_BLOCK_REACH_ID = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "block_reach");
    protected static final ResourceLocation BASE_ENTITY_REACH_ID = ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, "entity_reach");
    public BeastWeaponItem(Properties p_43381_) {
        super(p_43381_);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.95F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_KNOCKBACK_ID, (double) 1.75F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BASE_BLOCK_REACH_ID, (double) 1.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BASE_ENTITY_REACH_ID, (double) 1.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public boolean canAttackBlock(BlockState p_43409_, Level p_43410_, BlockPos p_43411_, Player p_43412_) {
        return !p_43412_.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {
        return true;
    }


    @Override
    public void postHurtEnemy(ItemStack p_345716_, LivingEntity p_345817_, LivingEntity p_346003_) {
        p_345716_.hurtAndBreak(1, p_346003_, EquipmentSlot.MAINHAND);
    }

    public boolean mineBlock(ItemStack p_43399_, Level p_43400_, BlockState p_43401_, BlockPos p_43402_, LivingEntity p_43403_) {
        if ((double) p_43401_.getDestroySpeed(p_43400_, p_43402_) != 0.0D) {
            p_43399_.hurtAndBreak(2, p_43403_, EquipmentSlot.MAINHAND);
        }
        return true;
    }


    public int getEnchantmentValue() {
        return 10;
    }
}
