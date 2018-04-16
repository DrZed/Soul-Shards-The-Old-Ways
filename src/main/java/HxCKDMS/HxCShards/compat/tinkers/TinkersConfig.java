package HxCKDMS.HxCShards.compat.tinkers;

import HxCKDMS.HxCShards.utils.Configs;
import hxckdms.hxcconfig.Config;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;

@Config
public class TinkersConfig {
    public static HashMap<String, TinkerMaterial> materials = new HashMap<>();

    public static void init() {
        materials.putIfAbsent("soulium", new TinkerMaterial(495, "soulium", Configs.souliumMaterialDurability, Math.round(Configs.souliumMaterialMiningSpeed), Configs.souliumMaterialHarvestLevel, Math.round(Configs.souliumMaterialDamage),
                1.0f, 4, 4, 5, 0.1f, EnumChatFormatting.DARK_PURPLE.toString(), 0xff660033, "ingotSoulium", 1, false, 1, 0, 0, 1, 2, 3));
        materials.putIfAbsent("improvedsoulium", new TinkerMaterial(496, "improvedsoulium", Configs.souliumMaterialDurability * 2, Math.round(Configs.souliumMaterialMiningSpeed * 1.5f), Configs.souliumMaterialHarvestLevel * 2, Math.round(Configs.souliumMaterialDamage * 1.3f),
                1.25f, 6, 3, 5, 0.1f, EnumChatFormatting.DARK_PURPLE.toString(), 0xff660000, "ingotSouliumImproved", 1, false, 2, 0, 0, 1.3f, 3, 4));
        materials.putIfAbsent("doomsoulium", new TinkerMaterial(497, "doomsoulium", Configs.souliumMaterialDurability * 5, Math.round(Configs.souliumMaterialMiningSpeed * 8f), Configs.souliumMaterialHarvestLevel * 5, Math.round(Configs.souliumMaterialDamage * 2.25f),
                3.0f, 10, 1, 5, 0.1f, EnumChatFormatting.DARK_PURPLE.toString(), 0xff880000, "ingotSouliumDraconic", 1, false, 5, 0, 0, 3, 5, 7));
    }
}
