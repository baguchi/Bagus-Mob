package baguchan.bagusmob;

import baguchan.bagusmob.entity.Ninjar;
import baguchan.bagusmob.entity.Tengu;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BagusMob.MODID)
public class EntityEvent {

	@SubscribeEvent()
	public static void addSpawn(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Villager) {
			Villager abstractVillager = (Villager) event.getEntity();

			abstractVillager.goalSelector.addGoal(1, new AvoidEntityGoal(abstractVillager, Tengu.class, 16.0F, 0.75F, 0.8F));
			abstractVillager.goalSelector.addGoal(1, new AvoidEntityGoal(abstractVillager, Ninjar.class, 16.0F, 0.75F, 0.8F));
		}

		if (event.getEntity() instanceof WanderingTrader) {
			WanderingTrader wanderingTraderEntity = (WanderingTrader) event.getEntity();

			wanderingTraderEntity.goalSelector.addGoal(1, new AvoidEntityGoal((PathfinderMob) wanderingTraderEntity, Tengu.class, 16.0F, 0.8F, 0.85F));
		}
	}
}