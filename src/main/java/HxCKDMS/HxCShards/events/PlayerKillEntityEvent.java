package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCShards.utils.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerKillEntityEvent {

	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
		World world = event.entity.worldObj;
		
		if (world.isRemote || !(event.source.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		EntityLiving dead = (EntityLiving) event.entity;
		EntityPlayer player = (EntityPlayer) event.source.getEntity();
		String entName = EntityList.getEntityString(dead);
		if (!Entitylist.wList.keySet().contains(entName) && !Entitylist.wList.get(entName)) {
			return;
		}
		
		if (entName == null || entName.isEmpty()) {
			LogHelper.debug(Utils.localizeFormatted("chat.hxcshards.debug.nounlocname", "" + dead), Reference.modName);
			return;
		}

		if (!EntityMapper.isEntityValid(entName)) {
			return;
		}

		if (dead instanceof EntitySkeleton
				&& ((EntitySkeleton) dead).getSkeletonType() == 1) {
			entName = "Wither Skeleton";
		}

		ItemStack shard = Utils.getShardFromInv(player, entName);

		if (shard != null) {
			if (!Utils.isShardBound(shard)) {
				Utils.setShardBoundEnt(shard, entName);
				Utils.writeEntityHeldItem(shard, dead);
				Utils.setShardBoundPlayer(shard, player);
			}
			Utils.writeEntityArmor(shard, dead);

			int soulStealer = EnchantmentHelper.getEnchantmentLevel(
					ModRegistry.SOUL_STEALER.effectId, player.getHeldItem());
			soulStealer *= Configs.enchantBonus;

			Utils.increaseShardKillCount(shard, (byte) (1 + soulStealer));
			Utils.checkForAchievements(player, shard);
		}
	}
}