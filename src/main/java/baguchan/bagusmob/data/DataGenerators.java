package baguchan.bagusmob.data;

import baguchan.bagusmob.BagusMob;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = BagusMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        event.getGenerator().addProvider(event.includeClient(), new ItemModelGenerators(packOutput, event.getExistingFileHelper()));

        BlockTagGenerator blocktags = new BlockTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper());
        event.getGenerator().addProvider(event.includeServer(), blocktags);

        event.getGenerator().addProvider(event.includeServer(), new ItemTagGenerator(packOutput, lookupProvider, blocktags.contentsGetter(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new EntityTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new DamageTypeTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper()));
    }
}