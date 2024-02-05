package baguchan.bagusmob.item;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.client.ModModelLayers;
import baguchan.bagusmob.client.model.NinjaArmorModel;
import baguchan.bagusmob.registry.ModArmorMaterials;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NinjaArmorItem extends ArmorItem {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BagusMob.MODID, "textures/models/ninja_armor.png");

    public NinjaArmorItem(ArmorItem.Type type, Item.Properties properties) {
        super(ModArmorMaterials.NINJA_ARMOR, type, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new NinjaArmorRender());
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return stack.is(ModItemRegistry.NINJA_BOOTS.get());
    }

    public static class NinjaArmorRender implements IClientItemExtensions {
        @Override
        public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            EntityModelSet models = Minecraft.getInstance().getEntityModels();
            ModelPart root = models.bakeLayer(ModModelLayers.NINJA_ARMOR);

            NinjaArmorModel<?> model2 = new NinjaArmorModel<>(root);
            ClientHooks.copyModelProperties(original, model2);
            this.setPartVisibility(model2, equipmentSlot);
            model2.setAllVisible(!livingEntity.isInvisible());

            return model2;
        }

        protected void setPartVisibility(HumanoidModel p_117126_, EquipmentSlot p_117127_) {
            p_117126_.setAllVisible(false);
            switch (p_117127_) {
                case HEAD:
                    p_117126_.head.visible = true;
                    p_117126_.hat.visible = true;
                    break;
                case CHEST:
                    p_117126_.body.visible = true;
                    p_117126_.rightArm.visible = true;
                    p_117126_.leftArm.visible = true;
                    break;
                case LEGS:
                    p_117126_.body.visible = true;
                    p_117126_.rightLeg.visible = true;
                    p_117126_.leftLeg.visible = true;
                    break;
                case FEET:
                    p_117126_.rightLeg.visible = true;
                    p_117126_.leftLeg.visible = true;
            }

        }
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TEXTURE.toString();
    }
}
