package HxCKDMS.HxCShards.utils;

import hxckdms.hxcconfig.Config;

import java.util.LinkedHashMap;

@Config
public class Configs {
	public static int enchantID = 52, enchantWeight = 8, enchantBonus = 1, spawnerBonus = 64,
            maxEntities = 80;

	public static boolean allowAbsorb = true, bindingAbsorb, debug, personalShard, invertRedstone,
            absorbAnySpawner, useSSTOWTextures, floodPrevention = true, BossMobShards = true, newColors;

	public static byte maxspawns = 8, maxDelay = 120, delayMultiplier = 20,
            spawnrange = 4, enchantMaxLevel = 5;

    public static float lightPercent = 0.25f, worldPercent = 0.75f, playerPercent = 0.5f, redstonePercent = 0.75f;

	public static LinkedHashMap<String, Tier> tiers = new LinkedHashMap<>();

	public void init() {
		tiers.putIfAbsent("Tier0", new Tier(0, (byte) 0, (byte) 120, true, true, true, false));
		tiers.putIfAbsent("Tier1", new Tier(64, (byte) 1, (byte) 120, true, true, true, false));
		for (byte i = 2; i <= 5; i++) {
			float percent = ((float) i / (float) 5);
			tiers.putIfAbsent("Tier" + i, new Tier(tiers.get("Tier" + (i - 1)).Kills * 2, ((byte) (percent * maxspawns) >= 1 ? (byte) (percent * maxspawns) : 1), (byte) (Math.round(maxDelay * (1 - percent)) + 1),
					percent < playerPercent, percent < lightPercent, percent < worldPercent, percent > redstonePercent));
		}

	}

	public class Tier {
		public int Kills;
		public byte Spawns;
		public byte Delay;
		public boolean PlayerCheck;
		public boolean LightCheck;
		public boolean WorldCheck;
		public boolean Redstone;

		public Tier() {}
		public Tier(int killCount, byte spawnCount, byte delayAmount, boolean checkPlayer, boolean checkLight, boolean checkWorld, boolean checkRedstone) {
			Kills = killCount;
			Spawns = spawnCount;
			Delay = delayAmount;
			PlayerCheck = checkPlayer;
			LightCheck = checkLight;
			WorldCheck = checkWorld;
			Redstone = checkRedstone;
		}
	}
}
