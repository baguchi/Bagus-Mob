package baguchan.tengu;

import baguchan.tengu.registry.TenguEntityRegistry;
import baguchan.tengu.registry.TenguItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TenguCore.MODID)
public class TenguCore
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tengu";
    public TenguCore()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        TenguItemRegistry.ITEM_REGISTRY.register(modEventBus);
        TenguEntityRegistry.ENTITIES_REGISTRY.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        Raid.RaiderType.create("tengu", TenguEntityRegistry.TENGU.get(), new int[]{0, 1, 2, 0, 2, 2, 3, 3});
    }
}
