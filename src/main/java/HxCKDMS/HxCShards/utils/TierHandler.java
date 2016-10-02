package HxCKDMS.HxCShards.utils;

import HxCKDMS.HxCCore.api.Utils.LogHelper;
import net.minecraft.item.ItemStack;

public final class TierHandler {
	public static boolean getChecksPlayer(byte tier) {
		return Configs.tiers.get("Tier" + tier).PlayerCheck;
	}

	public static boolean getChecksLight(byte tier) {
		return Configs.tiers.get("Tier" + tier).LightCheck;
	}

	public static boolean getChecksWorld(byte tier) {
		return Configs.tiers.get("Tier" + tier).WorldCheck;
	}

	public static boolean getChecksRedstone(byte tier) {
		return Configs.tiers.get("Tier" + tier).Redstone;
	}

	public static byte getNumSpawns(byte tier) {
		return Configs.tiers.get("Tier" + tier).Spawns;
	}

	public static byte getCooldown(byte tier) {
		return Configs.tiers.get("Tier" + tier).Delay;
	}

	public static int getMinKills(byte tier) {
		return Configs.tiers.get("Tier" + tier).Kills;
	}

	public static int getMaxKills(byte tier) {
        if (Configs.tiers.get("Tier" + (tier + 1)) != null)
			return Configs.tiers.get("Tier" + (tier + 1)).Kills - 1;
		return Configs.tiers.get("Tier" + tier).Kills;
	}

	public static boolean isShardValid(ItemStack shard) {
		int kills = Utils.getShardKillCount(shard);
		byte tier = Utils.getShardTier(shard);

		return kills >= getMinKills(tier) && kills <= getMaxKills(tier);
	}

	public static byte getCorrectTier(ItemStack shard) {
		int kills = Utils.getShardKillCount(shard);

		for (byte i = 0; i <= Configs.tiers.size(); i++)
			if (kills >= getMinKills(i) && kills <= getMaxKills(i))
                return i;

		LogHelper.debug(Utils.localizeFormatted("chat.hxcshards.debug.shardkillerror", "" + kills), Reference.modName);
		return 0;
	}
}
