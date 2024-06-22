package baguchan.bagusmob.registry;

import net.minecraft.world.entity.raid.Raid;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class RaiderEnumParams {
    @SuppressWarnings("unused")
    public static final EnumProxy<Raid.RaiderType> NINJAR = new EnumProxy<>(
            Raid.RaiderType.class, ModEntityRegistry.NINJAR, new int[]{0, 0, 1, 0, 1, 2, 2, 2}
    );
    @SuppressWarnings("unused")
    public static final EnumProxy<Raid.RaiderType> TENGU = new EnumProxy<>(
            Raid.RaiderType.class, ModEntityRegistry.TENGU, new int[]{0, 0, 1, 0, 1, 2, 3, 3}
    );
}
