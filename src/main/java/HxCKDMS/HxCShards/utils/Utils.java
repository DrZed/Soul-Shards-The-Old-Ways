package HxCKDMS.HxCShards.utils;

import HxCKDMS.HxCShards.events.Achievements;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public final class Utils {

	public static ItemStack getShardFromInv(EntityPlayer player, String entity) {
		ItemStack lastResort = null;

		for (int i = 0; i <= 8; i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);

			if (stack != null && stack.getItem() == ModRegistry.ItemSoulShard
					&& !hasMaxedKills(stack)) {
				if (!isShardBound(stack) && lastResort == null) {
					lastResort = stack;
				} else if (getShardBoundEnt(stack).equals(entity)) {
					return stack;
				}
			}
		}

		if (lastResort != null && lastResort.stackSize > 1) {
			--lastResort.stackSize;

			ItemStack newShard = new ItemStack(ModRegistry.ItemSoulShard, 1);

			for (int slot = 0; slot < player.inventory.mainInventory.length; slot++) {
				if (player.inventory.getStackInSlot(slot) == null) {
					player.inventory.addItemStackToInventory(newShard);
					return newShard;
				}
			}

			if (!Utils.isShardBound(newShard)) {
				Utils.setShardBoundEnt(newShard, entity);
				Utils.writeEntityHeldItem(newShard, (EntityLiving) EntityList.createEntityByName( entity, player.worldObj));
			}

			int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModRegistry.SOUL_STEALER.effectId, player.getHeldItem());
			soulStealer *= Configs.enchantBonus;
			Utils.increaseShardKillCount(newShard, (byte) (1 + soulStealer));
			Utils.checkForAchievements(player, newShard);
			player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, newShard));
			return null;
		}
		return lastResort;
	}

	public static void checkForAchievements(EntityPlayer player, ItemStack shard) {
		byte tier = getShardTier(shard);
		if (tier <= 15)
			player.addStat(Achievements.shards[tier], 1);
		else player.addStat(Achievements.shards[16], 1);
	}

	public static int getShardKillCount(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return 0;
		}

		return MathHelper.clamp_int(shard.stackTagCompound.getInteger("KillCount"), 0, Integer.MAX_VALUE);
	}

	public static void increaseShardKillCount(ItemStack shard, byte amount) {
		if (!shard.hasTagCompound() || hasMaxedKills(shard)) {
			return;
		}

		setShardKillCount(shard, getClampedKillCount(getShardKillCount(shard) + amount));
		checkAndFixShard(shard);
	}

	public static void checkAndFixShard(ItemStack shard) {
		if (!TierHandler.isShardValid(shard)) {
            setShardTier(shard, TierHandler.getCorrectTier(shard));
		}
	}

	public static void setShardKillCount(ItemStack shard, int value) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
		}

		shard.stackTagCompound.setInteger("KillCount", value);
	}

	public static byte getShardTier(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return 0;
		}
		return (byte) MathHelper.clamp_int(shard.stackTagCompound.getByte("Tier"), 0, Configs.numtiers);
	}

	public static void setShardTier(ItemStack shard, byte tier) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
		}

        shard.setItemDamage(MathHelper.clamp_int(tier, 0, Configs.numtiers));
		shard.stackTagCompound.setByte("Tier", (byte) MathHelper.clamp_int(tier, 0, Configs.numtiers));
	}

	/*
	 * Returns an empty string if unbound.
	 */
	public static String getShardBoundEnt(ItemStack shard) {
		if (!shard.hasTagCompound()) return "";
		return shard.stackTagCompound.getString("Entity");
	}

	/*
	 * Does not check if the shard is already bound!
	 */
	public static void setShardBoundEnt(ItemStack shard, String value) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
			shard.stackTagCompound.setDouble("antiStack",
					new Random().nextDouble());
		}

		shard.stackTagCompound.setString("Entity", value);
	}

	public static boolean isShardBound(ItemStack shard) {
		return !getShardBoundEnt(shard).isEmpty();
	}

	public static boolean hasMaxedKills(ItemStack shard) {
		return isShardBound(shard) && getShardKillCount(shard) >= TierHandler.getMaxKills(Configs.numtiers);
	}

	public static String getEntityNameTransltated(String unlocalName) {
		if (unlocalName.equals("Wither Skeleton")) {
			return unlocalName;
		}

		String result = StatCollector.translateToLocal("entity." + unlocalName + ".name");

		if (result == null) return unlocalName;

		return result;
	}

	private static int getClampedKillCount(int amount) {
		return MathHelper.clamp_int(amount, 0, TierHandler.getMaxKills(Configs.numtiers));
	}

	public static void writeEntityHeldItem(ItemStack shard, EntityLiving ent) {
		if (ent instanceof EntityZombie || ent instanceof EntityEnderman) {
			return;
		}

		ItemStack held = ent.getHeldItem();

		if (held != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			held.writeToNBT(nbt);

			if (nbt.hasKey("ench")) nbt.removeTag("ench");

			shard.stackTagCompound.setTag("HeldItem", nbt);
		}
	}

	public static ItemStack getEntityHeldItem(ItemStack shard) {
		if (shard.hasTagCompound() && shard.stackTagCompound.hasKey("HeldItem"))
			return ItemStack .loadItemStackFromNBT((NBTTagCompound) shard.stackTagCompound.getTag("HeldItem"));

		return null;
	}

	public static void writeEntityArmor(ItemStack shard, EntityLiving ent) {
		for (int i = 1; i <= 4; i++) {
			ItemStack armor = ent.getEquipmentInSlot(i);

			if (armor != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				armor.writeToNBT(nbt);

				if (nbt.hasKey("ench")) {
					nbt.removeTag("ench");
				}

				if (shard.stackTagCompound.hasKey("armor" + i)) {
					if (shard.stackTagCompound.getTag("armor" + i) != null) {
						NBTTagCompound oldnbt = (NBTTagCompound) shard.stackTagCompound
								.getTag("armor" + i);
						ItemStack oldArmor = ItemStack
								.loadItemStackFromNBT(oldnbt);
						if (oldArmor.getItem() != armor.getItem()) {
							shard.stackTagCompound.removeTag("armor" + i);
						}
					}
				} else {
					shard.stackTagCompound.setTag("armor" + i, nbt);
				}
			} else {
				shard.stackTagCompound.removeTag("armor" + i);
			}
		}
	}

	public static ItemStack getEntityArmor(ItemStack shard, int armorSlot) {
		if (shard.stackTagCompound.hasKey("armor" + armorSlot)
				&& shard.stackTagCompound.getTag("armor" + armorSlot) != null) {
			NBTTagCompound oldnbt = (NBTTagCompound) shard.stackTagCompound
					.getTag("armor" + armorSlot);
			return ItemStack.loadItemStackFromNBT(oldnbt);
		} else {
			return null;
		}
	}

	public static void setShardBoundPlayer(ItemStack shard, EntityPlayer player) {
		shard.stackTagCompound.setString("owner", player.getDisplayName());
        shard.setItemDamage(MathHelper.clamp_int(getShardTier(shard), 0, Configs.numtiers));
	}

	public static String getShardBoundPlayer(ItemStack shard) {
		if (shard.hasTagCompound() && shard.stackTagCompound.hasKey("owner"))
			return shard.stackTagCompound.getString("owner");

		return null;
	}

    private static final String LIGHT_GRAY = "\u00a77";
    private static final String YELLOW = "\u00a7e";
    private static final String ITALIC = "\u00a7o";
    private static final String END =  "\u00a7r";
    
    public static boolean displayShiftForDetail = true;
	
    public static String localize(String key) {
		return StatCollector.translateToLocal(key);
	}
	
	public static String localizeFormatted(String key, String keyFormat) {
		return String.format(localize(key), localize(keyFormat));
	}
	
	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	public static String shiftForDetails() {
		return LIGHT_GRAY + localize("info.sstow.tooltip.hold") + " " + YELLOW + ITALIC + localize("info.sstow.tooltip.shift") + " " + END + LIGHT_GRAY + localize("info.sstow.tooltip.forDetails") + END;
	}
	
	
	public static int pageSelector(int pages) {
		Random rand = new Random();
		return rand.nextInt((pages));
	}
}
