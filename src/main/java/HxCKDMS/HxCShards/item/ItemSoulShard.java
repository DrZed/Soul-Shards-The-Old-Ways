package HxCKDMS.HxCShards.item;

import HxCKDMS.HxCShards.utils.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class  ItemSoulShard extends Item {
    public ItemSoulShard() {
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!Utils.isShardBound(stack)) return "Unbound Soul Shard";
        if (Utils.getShardBoundEnt(stack).isEmpty()) return "Soul Shard \u00a76T" + Utils.getShardTier(stack);
        return Utils.getShardBoundEnt(stack) + " Soul Shard \u00a76T" + Utils.getShardTier(stack);
    }

    @Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {
		if (!world.isRemote){
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}

			if (stack.stackTagCompound.getBoolean("Anviled")) {
				EntityPlayer player = (EntityPlayer) entity;
				player.inventory.decrStackSize(slot, 0);
			}

			Utils.checkAndFixShard(stack);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		byte level = Utils.getShardTier(stack);
        byte tier = Utils.getShardTier(stack);

		if (!world.isRemote) {
			if(player.isSneaking()){
				if(!Utils.isShardBound(stack)){
					player.addChatComponentMessage(new ChatComponentText(Utils.localize("chat.hxcshards.shard.unbound")));
				} else if(Utils.getShardTier(stack) == 0){
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.owner", "" + Utils.getShardBoundPlayer(stack))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.level", "" + level)));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.bound", "" + Utils.getShardBoundEnt(stack))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localize("chat.hxcshards.shard.levelup")));
				} else {
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.level", "" + level)));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.owner", "" + Utils.getShardBoundPlayer(stack))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.bound", "" + Utils.getShardBoundEnt(stack))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.player", "" + TierHandler.getChecksPlayer(tier))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.light", "" + TierHandler.getChecksLight(tier))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.dimension", "" + TierHandler.getChecksWorld(tier))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.redstone", "" + TierHandler.getChecksRedstone(tier))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.quantity", "" + TierHandler.getNumSpawns(tier))));
					player.addChatComponentMessage(new ChatComponentText(Utils.localizeFormatted("chat.hxcshards.shard.rate", "" + TierHandler.getCooldown(tier))));
				}
			}
		}

		if (world.isRemote || (Utils.hasMaxedKills(stack)) || !Configs.allowAbsorb)
			return stack;
		
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

		if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
			return stack;
		}

		TileEntity tile = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);

		if (tile instanceof TileEntityMobSpawner) {
			String name = ((TileEntityMobSpawner) tile).func_145881_a().getEntityNameToSpawn();

			Entity ent = EntityMapper.getNewEntityInstance(world, name);

			if (ent == null) {
				return stack;
			}

			ent = ((TileEntityMobSpawner) tile).func_145881_a().func_98265_a(ent);

			if (ent instanceof EntitySkeleton && ((EntitySkeleton) ent).getSkeletonType() == 1) {
				name = "Wither Skeleton";
			}

			if (!EntityMapper.isEntityValid(name)) {
				return stack;
			}

			if (Utils.isShardBound(stack)) {
				if (Configs.absorbAnySpawner || Utils.getShardBoundEnt(stack).equals(name)) {
					Utils.increaseShardKillCount(stack, (byte) Configs.spawnerBonus);
//					Utils.checkForAchievements(player, stack);
					world.func_147480_a(mop.blockX, mop.blockY, mop.blockZ, false);
				}
			} else if (EntityMapper.isEntityValid(name)) {
				if (stack.stackSize > 1) {
					stack.stackSize--;
					ItemStack newStack = new ItemStack(ModRegistry.ItemSoulShard, 1);

					Utils.setShardBoundEnt(newStack, name);
					Utils.writeEntityHeldItem(newStack, (EntityLiving) ent);
					Utils.increaseShardKillCount(newStack,
							(byte) Configs.spawnerBonus);

					boolean emptySpot = false;
					int counter = 0;

					while (!emptySpot && counter < 36) {
						ItemStack inventoryStack = player.inventory
								.getStackInSlot(counter);
						if (inventoryStack == null) {
							player.inventory.addItemStackToInventory(newStack);
							emptySpot = true;
						}
						counter++;
					}
					world.func_147480_a(mop.blockX, mop.blockY, mop.blockZ, true);
					if (!emptySpot) {
						player.worldObj.spawnEntityInWorld(new EntityItem(
								player.worldObj, player.posX, player.posY,
								player.posZ, newStack));
					}
				} else {
					if(Configs.bindingAbsorb){
						Utils.setShardBoundEnt(stack, name);
						Utils.writeEntityHeldItem(stack, (EntityLiving) ent);
						Utils.increaseShardKillCount(stack, (byte) Configs.spawnerBonus);
					}
				}
			}
		}

		return stack;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "shard_tier_" + Utils.getShardTier(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return Utils.hasMaxedKills(stack);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (byte i = 0; i < Configs.tiers.size(); i++) {
			ItemStack stack = new ItemStack(item, 1);

			Utils.setShardKillCount(stack, TierHandler.getMinKills(i));
			Utils.setShardTier(stack, i);

			list.add(stack);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		if (Utils.isShardBound(stack))
			list.add("Bound to: " + Utils.getEntityNameTransltated(Utils.getShardBoundEnt(stack)));

		if (Utils.getShardKillCount(stack) >= 0) list.add("Kills: " + Utils.getShardKillCount(stack));

		list.add("Tier: " + Utils.getShardTier(stack));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		return itemIcon;
	}

    @Override
    public int getColorFromItemStack(ItemStack shard, int renderpass) {
        if (Configs.newColors && Configs.tiers.size() > 10) {
            float percentile = (float) shard.getItemDamage() / (float) Configs.tiers.size();
            if (percentile < 0.5f)
                return new Color(1 - percentile, 0f, 1 - percentile).getRGB();
            else return new Color(percentile - 0.15f, 0f, 0f).getRGB();
        } else {
            float percentile = (float) shard.getItemDamage() / (float) Configs.tiers.size();

            return new Color(Math.max(Math.min(percentile, 0.9f), 0.45f), 0f, Math.max(Math.min(percentile, 0.9f), 0.45f)).getRGB();
        }
    }

    @Override
    public int getDamage(ItemStack stack) {
        return Utils.getShardTier(stack);
    }

    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		itemIcon = register.registerIcon(Reference.modID + ":soul_shard" + (Configs.useSSTOWTextures ? "_old" : ""));
	}
}
