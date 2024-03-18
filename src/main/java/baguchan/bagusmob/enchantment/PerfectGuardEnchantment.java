package baguchan.bagusmob.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

import static baguchan.bagusmob.registry.ModEnchantments.KATANA_CATEGORY;

public class PerfectGuardEnchantment extends Enchantment {
    public PerfectGuardEnchantment(Enchantment.Rarity p_i50019_1_, EquipmentSlot... p_i50019_2_) {
        super(p_i50019_1_, KATANA_CATEGORY, p_i50019_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 20;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50 + this.getMinCost(p_223551_1_);
    }

    public int getMaxLevel() {
        return 1;
    }

	/*public boolean checkCompatibility(Enchantment p_77326_1_) {
		return super.checkCompatibility(p_77326_1_) && p_77326_1_ != Enchantments.MULTISHOT;
	}*/
}