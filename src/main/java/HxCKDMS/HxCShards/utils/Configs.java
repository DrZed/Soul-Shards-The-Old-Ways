package HxCKDMS.HxCShards.utils;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.LinkedHashMap;

@Config
public class Configs {
	public static int enchantID = 52, enchantWeight = 8, enchantBonus = 1, spawnerBonus = 64,
            maxEntities = 80;

	public static boolean allowAbsorb = true, bindingAbsorb, debug, personalShard, invertRedstone,
            absorbAnySpawner, useSSTOWTextures, floodPrevention = true, BossMobShards = true, newColors;

	public static byte maxspawns = 8, maxDelay = 120, delayMultiplier = 20,
            spawnrange = 4, enchantMaxLevel = 5;

    public static float lightPercent = 0.25f, worldPercent = 0.75f, playerPercent = 0.5f, redstonePercent = 0.25f;

	public static LinkedHashMap<String, Tier> tiers = new LinkedHashMap<>();

	static {
        tiers.putIfAbsent("Tier0", newTier(0, (byte) 0, (byte) 120, true, true, true, false));
		tiers.putIfAbsent("Tier1", newTier(64, (byte) 1, (byte) 120, true, true, true, false));
		for (byte i = 2; i <= 5; i++) {
			tiers.putIfAbsent("Tier" + i, newTier(tiers.get("Tier" + (i-1)).Kills * 2, ((float)i/(float)5)));
		}
//        if (Configurations.DebugMode)
//            tiers.forEach((tier, t) ->
//                LogHelper.debug(tier + " Registered with \n Kills : " + t.Kills + "  \n Delay : " + t.Delay + " \n Checks Light : " +
//                        t.LightCheck + " \n Checks World : " + t.WorldCheck + " \n Redstone Check : " + t.Redstone +
//                        " \n Player Check : " + t.PlayerCheck + " \n Spawns : " + t.Spawns, Reference.modName)
//            );
	}

	public static Tier newTier(int KillsRequired, byte Spawns, byte Delay, boolean light, boolean player, boolean world, boolean redstone) {
		Tier t = new Tier();
        t.Delay = Delay;
		t.Kills = KillsRequired > 8 ? KillsRequired : 0;
		t.LightCheck = light;
		t.WorldCheck = world;
		t.Spawns = Spawns;
		t.PlayerCheck = player;
		t.Redstone = redstone;
		return t;
	}

	public static Tier newTier(int KillsRequired, float percent) {
		Tier t = new Tier();
		t.Delay = (byte)(Math.round(maxDelay * (1-percent)) + 1);
		t.Kills = KillsRequired;
		t.LightCheck = percent < lightPercent;
		t.WorldCheck = percent < worldPercent;
		t.Spawns = ((byte)(percent * maxspawns) >= 1 ? (byte)(percent * maxspawns) : 1);
		t.PlayerCheck = percent < playerPercent;
		t.Redstone = percent > redstonePercent;
        return t;
	}
}
