package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.HxCShards;
import HxCKDMS.HxCShards.utils.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.List;

public class PlayerKillEntityEvent {

	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
        if ((event.source.getEntity() instanceof EntityPlayerMP) && !(event.entityLiving instanceof EntityPlayer) && event.entityLiving instanceof EntityLiving) {
            World world = event.entity.worldObj;
            EntityLiving victim = (EntityLiving) event.entityLiving;
            EntityPlayerMP player = (EntityPlayerMP) event.source.getEntity();

            if (world.isRemote)
                return;

            String entName = "";
			try {
				entName = EntityList.getEntityString(victim);
			} catch (Exception e) {
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
                if (player.getHeldItem().getItem() == ModRegistry.ItemSwordSoul2)
                    Utils.increaseShardKillCount(shard, (byte) (2));
                else if (player.getHeldItem().getItem() == ModRegistry.ItemSwordSoul2)
                    Utils.increaseShardKillCount(shard, (byte) (5));
                Utils.checkForAchievements(player, shard);
            }
        }
        if (event.source.getEntity() instanceof EntityPlayer && ((EntityPlayer) event.source.getEntity()).getHeldItem().getItem() == ModRegistry.ItemSwordSoul3) {
            ItemStack sword = ((EntityPlayer) event.source.getEntity()).getHeldItem();
            if (event.entityLiving instanceof IBossDisplayData)
                killstreak((EntityPlayer) event.source.getEntity(), sword, true);
            else
                killstreak((EntityPlayer) event.source.getEntity(), sword, false);
        } else if (event.source instanceof DamageSourceDoom && ((DamageSourceDoom) event.source).player.getHeldItem().getItem() == ModRegistry.ItemSwordSoul3) {
            ItemStack sword = ((DamageSourceDoom) event.source).player.getHeldItem();
            if (event.entityLiving instanceof IBossDisplayData)
                killstreak(((DamageSourceDoom) event.source).player, sword, true);
            else
                killstreak(((DamageSourceDoom) event.source).player, sword, false);
        }
    }

    private void killstreak(EntityPlayer player, ItemStack sword, boolean boss) {
        int diff = player.worldObj.difficultySetting.getDifficultyId();
        int bonus = player.worldObj.rand.nextInt(100 + 25 * diff);
        if (bonus < 25) {bonus = -diff;}
        else if (bonus < 75) {bonus = 1;}
        else {bonus = 0;}
        if (boss) bonus += diff;
        if (sword.hasTagCompound() && sword.getTagCompound().getInteger("Killstreak") + bonus <= 0) {
            bonus = 1;
        } else {
            bonus = sword.getTagCompound().getInteger("Killstreak") + bonus;
        }
        if (sword.hasTagCompound()) {
            NBTTagCompound tag = sword.getTagCompound();
            tag.setInteger("Killstreak", bonus);
            sword.setTagCompound(tag);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Killstreak", bonus);
            sword.setTagCompound(tag);
        }
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
	    if (event.entityPlayer.getHeldItem().getItem() == ModRegistry.ItemSwordSoul3 && event.entityPlayer.getHeldItem().hasTagCompound() && event.entityPlayer.getHeldItem().getTagCompound().hasKey("Killstreak")) {
	        event.target.attackEntityFrom(new DamageSourceDoom("doomblade", event.entityPlayer), event.entityPlayer.getHeldItem().getTagCompound().getInteger("Killstreak"));
        }
    }

    @SubscribeEvent
    public void entityAttacked(LivingAttackEvent event) {
	    if (event.source.damageType.equalsIgnoreCase("doomblade")) {
	        DamageSourceDoom sourceDoom = (DamageSourceDoom) event.source;
            List<EntityMob> mobs = event.entity.worldObj.getEntitiesWithinAABB(EntityMob.class, AABBUtils.getAreaBoundingBox((int) event.entity.posX, (int) event.entity.posY, (int) event.entity.posZ, 10));
            mobs.forEach(mob -> {
                DamageSource dmg = new DamageSourceDoom("doomblade_aoe", sourceDoom.player);
                mob.attackEntityFrom(dmg, event.ammount);
            });
        }
    }
}