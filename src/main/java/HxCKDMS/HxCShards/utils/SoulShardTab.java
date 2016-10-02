package HxCKDMS.HxCShards.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulShardTab extends CreativeTabs {

	public SoulShardTab() {
		super(Reference.modID);
	}

	@Override
	public ItemStack getIconItemStack() {
		ItemStack shard = new ItemStack(ModRegistry.ItemSoulShard);

		Utils.setShardTier(shard, (byte)Configs.tiers.size());
		Utils.setShardKillCount(shard, TierHandler.getMaxKills((byte) (Configs.tiers.size()-1)));
		Utils.setShardBoundEnt(shard, "NULL");

		return shard;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}
}
