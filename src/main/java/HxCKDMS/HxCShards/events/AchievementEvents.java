package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.utils.ModRegistry;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class AchievementEvents {

	
	
	
	
	@SubscribeEvent
	public void smeltingAchievement(PlayerEvent.ItemSmeltedEvent event) {
		if (event.smelting.getItem().equals(new ItemStack(ModRegistry.ItemMaterials, 1, 3))) {
			event.player.addStat(Achievements.viledust, 1);
		}
	}
//
//	@SubscribeEvent
//	public void craftingAchievements(ItemCraftedEvent event) {
//		if (event.crafting.getItem().equals(new ItemStack(ModRegistry.ItemMaterials, 1, 4))) {
//			event.player.addStat(Achievements.corruption, 1);
//		}
//		
//		if (event.crafting.getItem() == Item.getItemFromBlock(ModRegistry.BlockForge)) {
//			event.player.addStat(Achievements.soulforge, 1);
//		}
//		
//		if (event.crafting.getItem() == Item.getItemFromBlock(ModRegistry.BlockCage)) {
//			event.player.addStat(Achievements.soulcage, 1);
//		}
//	}
//
//	@SubscribeEvent
//	public void pickupAchievements(ItemPickupEvent event) {
//		if (event.pickedUp.getEntityItem().getItem().equals(ModRegistry.ItemSoulShard)) {
//			event.player.addStat(Achievements.unboundshard, 1);
//		}
//	}
}
