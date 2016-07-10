package HxCKDMS.HxCShards.utils;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.HashMap;

@Config
public class Configs {
	// Enchant Section
	public static int enchantID = 52, enchantWeight = 8, enchantBonus = 1;

	// General Section
	public static int spawnerBonus = 64;
	public static boolean allowAbsorb = true, bindingAbsorb, debug, personalShard, unbreakableSoulSpawner;

	// Soul Cage
	public static int maxEntities = 80;
	public static boolean invertRedstone, absorbAnySpawner, useSSTOWTextures, floodPrevention = true;

	// Recipes Section
	public static int cookingModifier = 10, tier1Kills = 64;

	public static byte numtiers = 16, maxspawns = 8, maxDelay = 120, delayMultiplier = 20, spawnrange = 4, enchantMaxLevel = 5;

    @Config.ignore
	public static HashMap<String, Tier> tiers = new HashMap<>();

	public static byte registerShards() {
        tiers.put("Tier0", newTier(0, (byte) 0, maxDelay, true, true, true, false));
		tiers.put("Tier1", newTier(tier1Kills, (byte) 1, maxDelay, true, true, true, false));
		for (byte i = 2; i <= numtiers; i++) {
			float percentile = i/numtiers;
			tiers.put("Tier" + i, newTier(tiers.get("Tier" + String.valueOf(i - 1)).Kills * 2, percentile));
		}
        return (byte)tiers.size();
	}

	public static Tier newTier(int KillsRequired, byte Spawns, byte Delay, boolean light, boolean player, boolean world, boolean redstone) {
		Tier t = new Tier();
		t.Delay = Delay;
		t.Kills = KillsRequired;
		t.LightCheck = light;
		t.WorldCheck = world;
		t.Spawns = Spawns;
		t.PlayerCheck = player;
		t.Redstone = redstone;
		return t;
	}

	public static Tier newTier(int KillsRequired, float percent) {
		Tier t = new Tier();
		t.Delay = (byte)((maxDelay - (percent * maxDelay)) + 1);
		t.Kills = KillsRequired;
		t.LightCheck = percent < 0.3f;
		t.WorldCheck = percent < 0.7f;
		t.Spawns = (byte)(percent * maxspawns);
		t.PlayerCheck = percent < 0.5f;
		t.Redstone = percent > 0.6f;
		return t;
	}
}
