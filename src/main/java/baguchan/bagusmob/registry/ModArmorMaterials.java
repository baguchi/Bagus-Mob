package baguchan.bagusmob.registry;

import baguchan.bagusmob.BagusMob;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, BagusMob.MODID);

    public static final Holder<ArmorMaterial> NINJA_ARMOR = register(
            "ninja_armor",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 2);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 4);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 5);
                p_323384_.put(ArmorItem.Type.HELMET, 2);
                p_323384_.put(ArmorItem.Type.BODY, 10);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.LEATHER)
    );

    private static Holder<ArmorMaterial> register(
            String p_323589_,
            EnumMap<ArmorItem.Type, Integer> p_323819_,
            int p_323636_,
            Holder<SoundEvent> p_323958_,
            float p_323937_,
            float p_323731_,
            Supplier<Ingredient> p_323970_
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(BagusMob.MODID, p_323589_)));
        return register(p_323589_, p_323819_, p_323636_, p_323958_, p_323937_, p_323731_, p_323970_, list);
    }

    private static Holder<ArmorMaterial> register(
            String p_323865_,
            EnumMap<ArmorItem.Type, Integer> p_324599_,
            int p_324319_,
            Holder<SoundEvent> p_324145_,
            float p_323494_,
            float p_324549_,
            Supplier<Ingredient> p_323845_,
            List<ArmorMaterial.Layer> p_323990_
    ) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
            enummap.put(armoritem$type, p_324599_.get(armoritem$type));
        }

        return ARMOR_MATERIALS.register(p_323865_,
                () -> new ArmorMaterial(enummap, p_324319_, p_324145_, p_323845_, p_323990_, p_323494_, p_324549_)
        );
    }
}