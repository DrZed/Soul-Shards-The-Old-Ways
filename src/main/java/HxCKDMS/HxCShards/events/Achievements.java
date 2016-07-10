package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.utils.Configs;
import HxCKDMS.HxCShards.utils.ModRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;

import HxCKDMS.HxCShards.utils.Utils;
import org.apache.commons.lang3.ArrayUtils;

public class Achievements {
	public static Achievement soulcage, viledust, soulforge;
	public static Achievement[] shards = new Achievement[17];

	private static void defaultAchievements() {
        soulforge = new Achievement("achievement.soulForge", "soulForge", -1, -3, ModRegistry.BlockForge, AchievementList.acquireIron).initIndependentStat().registerStat();
		viledust = new Achievement("achievement.vile_dust", "vile_dust", -1, 1, new ItemStack(ModRegistry.ItemMaterials, 1, 3), soulforge).registerStat();
		soulcage = new Achievement("achievement.createCage", "createCage", 0, 0, ModRegistry.BlockCage, soulforge).registerStat();

        ItemStack s0 = new ItemStack(ModRegistry.ItemSoulShard, 0);
        Utils.setShardTier(s0, (byte) 0);
        Utils.setShardBoundEnt(s0, "NULL");

		shards[0] = new Achievement("achievement.createShard", "createShard", 4, 1, s0, soulforge).registerStat();

		ItemStack sx = new ItemStack(ModRegistry.ItemSoulShard, Configs.numtiers);
		Utils.setShardTier(sx, Configs.numtiers);
		Utils.setShardBoundEnt(sx, "NULL");

        if (Configs.numtiers > 15)
		    shards[16] = new Achievement("achievement.shard_tier_X", "shard_tier_X", 2 * 16, -2 * 16, sx, shards[15]).registerStat();

		for (byte tier = 1; tier < Configs.numtiers; tier++) {
            if (tier > 15) break;
			ItemStack shard = new ItemStack(ModRegistry.ItemSoulShard, tier);
			Utils.setShardTier(shard, tier);
			Utils.setShardBoundEnt(shard, "NULL");

			shards[tier] = new Achievement("achievement.shard_tier_" + tier, "shard_tier_" + tier, 2 * tier, -2 * tier, shard, shards[tier-1]).registerStat();
		}
	}

	private static void defaultAchievementPages() {
		AchievementPage.registerAchievementPage(new AchievementPage("HxC Soul Shards", ArrayUtils.addAll(new Achievement[]{viledust, soulforge, soulcage}, shards)));
	}

	public static void Get() {
		defaultAchievements();
		defaultAchievementPages();
	}
}