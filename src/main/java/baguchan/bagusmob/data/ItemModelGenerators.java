package baguchan.bagusmob.data;

import baguchan.bagusmob.BagusMob;
import baguchan.bagusmob.registry.ModItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ItemModelGenerators extends ItemModelProvider {
    public ItemModelGenerators(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, BagusMob.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTex(ModItemRegistry.SNAKE_POTTERY_SHERD);
        singleTex(ModItemRegistry.NINJA_HOOD);
        singleTex(ModItemRegistry.NINJA_CHESTPLATE);
        singleTex(ModItemRegistry.NINJA_BOOTS);
        singleTexTool(ModItemRegistry.SHARPED_LEAF);
        singleTexTool(ModItemRegistry.DAGGER);

        egg(ModItemRegistry.BURNER_HOG_SPAWNEGG);
        egg(ModItemRegistry.HUNTER_BOAR_SPAWNEGG);
        egg(ModItemRegistry.RUDEHOG_SPAWNEGG);
        egg(ModItemRegistry.NINJAR_SPAWNEGG);
        egg(ModItemRegistry.POT_SNAKE_SPAWNEGG);
        egg(ModItemRegistry.SOILTH_SPAWNEGG);
        egg(ModItemRegistry.TENGU_SPAWNEGG);
    }

    private ItemModelBuilder generated(String name, ResourceLocation... layers) {
        return buildItem(name, "item/generated", 0, layers);
    }


    private ItemModelBuilder buildItem(String name, String parent, int emissivity, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, parent);
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        if (emissivity > 0)
            builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("minecraft:translucent", 0).end();
        return builder;
    }

    private ItemModelBuilder tool(String name, ResourceLocation... layers) {
        return buildItem(name, "item/handheld", 0, layers);
    }

    private ItemModelBuilder singleTexTool(Supplier<? extends Item> item) {
        return tool(itemPath(item).getPath(), modLoc("item/" + itemPath(item).getPath()));
    }

    private ItemModelBuilder singleTexRodTool(Supplier<? extends Item> item) {
        return toolRod(itemPath(item).getPath(), modLoc("item/" + itemPath(item).getPath()));
    }

    private ItemModelBuilder toolRod(String name, ResourceLocation... layers) {
        return buildItem(name, "item/handheld_rod", 0, layers);
    }

    private ItemModelBuilder singleTex(Supplier<? extends ItemLike> item) {
        return generated(itemPath(item).getPath(), modLoc("item/" + itemPath(item).getPath()));
    }


    private ItemModelBuilder toBlock(Supplier<? extends Block> b) {
        return toBlockModel(b, BuiltInRegistries.BLOCK.getKey(b.get()).getPath());
    }

    private ItemModelBuilder toBlockModel(Supplier<? extends Block> b, String model) {
        return toBlockModel(b, modLoc("block/" + model));
    }

    private ItemModelBuilder toBlockModel(Supplier<? extends Block> b, ResourceLocation model) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(b.get()).getPath(), model);
    }

    public ItemModelBuilder itemBlockFlat(Supplier<? extends Block> block) {
        return itemBlockFlat(block, blockName(block));
    }

    public ItemModelBuilder itemBlockFlat(Supplier<? extends Block> block, String name) {
        return withExistingParent(blockName(block), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + name));
    }

    public ItemModelBuilder egg(Supplier<Item> item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), mcLoc("item/template_spawn_egg"));
    }

    public String blockName(Supplier<? extends Block> block) {
        return BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
    }

    private ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    public ResourceLocation itemPath(Supplier<? extends ItemLike> item) {
        return BuiltInRegistries.ITEM.getKey(item.get().asItem());
    }
}
