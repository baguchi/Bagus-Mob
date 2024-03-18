package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.enchantment.PerfectGuardEnchantment;
import baguchan.bagusmob.item.KatanaItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, BagusMob.MODID);


    public static final EnchantmentCategory KATANA_CATEGORY = EnchantmentCategory.create("katana", (item) -> {
        return item instanceof KatanaItem;
    });

    public static final Supplier<Enchantment> PERFECT_GUARD = ENCHANTMENTS.register("perfect_guard", () -> new PerfectGuardEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

}