package baguchan.wasabi;

import baguchan.wasabi.registry.TenguEntityRegistry;
import baguchan.wasabi.registry.TenguItemRegistry;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Wasabi.MODID)
public class Wasabi {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wasabi";

    public Wasabi() {
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
