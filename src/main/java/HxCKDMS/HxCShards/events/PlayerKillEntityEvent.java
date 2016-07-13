package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.utils.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerKillEntityEvent {

	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
        if ((event.source.getEntity() instanceof EntityPlayerMP) && !(event.entityLiving instanceof EntityPlayer) && event.entityLiving instanceof EntityLiving) {
            World world = event.entity.worldObj;
            EntityLiving victim = (EntityLiving) event.entityLiving;
            EntityPlayerMP player = (EntityPlayerMP) event.source.getEntity();

            if (Configurations.DebugMode)
                LogHelper.warn("Victim : " + victim.getCommandSenderName() + " | " + victim.getClass().getTypeName() + " | " + victim.getClass().getName()
                        + " | " + victim.getClass().getCanonicalName() + " | " + victim.getClass().getSimpleName() + "\n Player : " +
                        player.getDisplayName() + " | " + player.getHeldItem().getUnlocalizedName(), Reference.modName);

            if (world.isRemote)
                return;

            String entName = "";
			try {
				entName = EntityList.getEntityString(victim);
			} catch (Exception e) {
                LogHelper.debug(victim.getCommandSenderName() + " Failed to find in wList or not in EntityList", Reference.modName);
                if (!HxCShards.logDump.containsKey(victim.getCommandSenderName()))
                    HxCShards.logDump.putIfAbsent(victim.getCommandSenderName(), "Victim : " + victim.getCommandSenderName() + " | " + victim.getClass().getTypeName() + " | " + victim.getClass().getName()
                    + " | " + victim.getClass().getCanonicalName() + " | " + victim.getClass().getSimpleName() + " Player : " +
                    player.getDisplayName() + " | " + player.getHeldItem().getUnlocalizedName());
				e.printStackTrace();
			}

            if (entName.isEmpty() || (!Entitylist.wList.keySet().contains(entName) || !Entitylist.wList.get(entName)) || !EntityMapper.isEntityValid(entName)) {
                if (!HxCShards.logDump.containsKey(victim.getCommandSenderName()))
                    HxCShards.logDump.putIfAbsent(victim.getCommandSenderName(), "Victim : " + victim.getCommandSenderName() + " | " + victim.getClass().getTypeName() + " | " + victim.getClass().getName()
                    + " | " + victim.getClass().getCanonicalName() + " | " + victim.getClass().getSimpleName() + " Player : " +
                    player.getDisplayName() + " | " + player.getHeldItem().getUnlocalizedName());
                LogHelper.debug(victim.getCommandSenderName() + " Failed to find in wList or not in EntityList", Reference.modName);
                return;
            }

            if (victim instanceof EntitySkeleton && ((EntitySkeleton) victim).getSkeletonType() == 1) {
                entName = "Wither Skeleton";
            }

            ItemStack shard = Utils.getShardFromInv(player, entName);

            if (shard != null) {
                if (!Utils.isShardBound(shard)) {
                    Utils.setShardBoundEnt(shard, entName);
                    Utils.writeEntityHeldItem(shard, victim);
                    Utils.setShardBoundPlayer(shard, player);
                }

                Utils.writeEntityArmor(shard, victim);

                int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModRegistry.SOUL_STEALER.effectId, player.getHeldItem());
                soulStealer *= Configs.enchantBonus;

                Utils.increaseShardKillCount(shard, (byte) (1 + soulStealer));
                Utils.checkForAchievements(player, shard);
            }
        }
    }
}