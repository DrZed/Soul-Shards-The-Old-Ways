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

		Utils.setShardTier(shard, Configs.numtiers);
		Utils.setShardKillCount(shard, TierHandler.getMaxKills(Configs.numtiers));
		Utils.setShardBoundEnt(shard, "NULL");

		return shard;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}
}
