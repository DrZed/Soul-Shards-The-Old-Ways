package HxCKDMS.HxCShards.tileentity;

import HxCKDMS.HxCShards.utils.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileEntityCage extends TileEntity implements ISidedInventory {

    public ItemStack shard;
    public int disableTimer = 0;

	private int counter;
    private int delay = 10000;
	private int updateCounter;
	private byte tier;
	private static final int slot = 0;

	private boolean redstoneActive;
	private boolean active;

	private String entName = "";
	private String owner = "";
	private String cageName;

	public TileEntityCage() {
		counter = 0;
		updateCounter = 0;
		redstoneActive = false;
		active = false;
	}

	@Override
	public void updateEntity() {
        if (disableTimer > 0) {
            disableTimer--;
            return;
        }
		if (worldObj.isRemote || entName.isEmpty() || shard == null)
			return;

		if (!EntityList.wList.keySet().contains(entName) && !EntityList.wList.get(entName) || shard == null)
			return;

        checkRedstone();

        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, (active ? 1 : 0), 3);

        if (shard != null && (redstoneActive == !Configs.invertRedstone) && TierHandler.getChecksRedstone(Utils.getShardTier(shard))) {
            counter = 0;
            active = false;
            return;
        } else active = true;

        if (shard != null && TierHandler.getChecksLight(Utils.getShardTier(shard)) && !canSpawnInLight(EntityMapper.getNewEntityInstance(worldObj, entName))) {
            counter = 0;
            active = false;
            return;
        } else active = true;

        if (shard != null && TierHandler.getChecksWorld(Utils.getShardTier(shard)) && !canSpawnInWorld(EntityMapper.getNewEntityInstance(worldObj, entName))) {
            counter = 0;
            active = false;
            return;
        } else active = true;

        if (shard != null && TierHandler.getChecksPlayer(Utils.getShardTier(shard)) && !isPlayerClose(xCoord, yCoord, zCoord)) {
            counter = 0;
            active = false;
            return;
        } else active = true;

		if (shard != null && tier <= 0) {
			updateCounter = 0;
			counter = 0;
			return;
		} else active = true;

		if (updateCounter == 19) {
			EntityLiving ent = EntityMapper.getNewEntityInstance(this.worldObj, entName);
			active = canEntitySpawn(ent) && shard != null;
			updateCounter = 0;
            delay = TierHandler.getCooldown(tier) * Configs.delayMultiplier;
        } else updateCounter += 1;


		if (shard != null && counter >= delay) {
			EntityLiving[] toSpawn = new EntityLiving[TierHandler.getNumSpawns(tier)];

			ItemStack heldItem = Utils.getEntityHeldItem(shard);
			for (int i = 0; i < toSpawn.length; i++) {
				toSpawn[i] = EntityMapper.getNewEntityInstance(this.worldObj, entName);

				if ((toSpawn[i] instanceof EntitySlime))
					toSpawn[i].getDataWatcher().updateObject(16, (byte) 1);

				if (heldItem != null)
					toSpawn[i].setCurrentItemOrArmor(0, heldItem);

				for (int j = 1; j <= 4; j++)
					toSpawn[i].setCurrentItemOrArmor(j, Utils.getEntityArmor(shard, j));

				toSpawn[i].getEntityData().setBoolean("SoulShard", true);
				toSpawn[i].forceSpawn = true;
				toSpawn[i].func_110163_bv();
			}
			spawnEntities(toSpawn);
			counter = 0;
		}
        counter++;
    }

	public void checkRedstone() {
		redstoneActive = worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
	}

	private boolean canEntitySpawn(EntityLiving ent) {
		return !((Configs.floodPrevention) && (hasReachedSpawnLimit(ent)));
	}

    @Override
    public int getBlockMetadata() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    private boolean isPlayerClose(int x, int y, int z) {
		EntityPlayer entityPlayer = worldObj.getClosestPlayer(x, y, z, 16.0D);
		return (Configs.personalShard && entityPlayer != null && entityPlayer.getDisplayName().equals(owner))
				|| (!Configs.personalShard && entityPlayer != null);
	}

	private boolean canSpawnInWorld(EntityLiving ent) {
		int dimension = worldObj.provider.dimensionId;

		if ((ent instanceof EntitySkeleton)) {
			EntitySkeleton skele = (EntitySkeleton) ent;
			return (skele.getSkeletonType() == 1) && dimension == -1;
		}

		if (((ent instanceof EntityBlaze))
				|| ((ent instanceof EntityPigZombie))
				|| ((ent instanceof EntityGhast))
				|| ((ent instanceof EntityMagmaCube))) {
			return dimension == -1;
		}

		return !(ent instanceof EntityEnderman) || dimension == 1;
	}

	private boolean canSpawnInLight(EntityLiving ent) {
		int light = worldObj.getBlockLightValue(xCoord, yCoord, zCoord);
		if (((ent instanceof EntityMob)) || ((ent instanceof IMob)))
			return light <= 7;
		return !(((ent instanceof EntityAnimal)) || ((ent instanceof IAnimals))) || light >= 8;
	}

	private boolean canSpawnAtCoords(EntityLiving ent) {
		return worldObj.getCollidingBoundingBoxes(ent, ent.boundingBox).isEmpty();
	}

	@SuppressWarnings("unchecked")
	private boolean hasReachedSpawnLimit(EntityLiving ent) {
		AxisAlignedBB aabb = AABBUtils.getAreaBoundingBox(xCoord, yCoord, zCoord, 16);
		int mobCount = 0;

		for (EntityLiving entity : (List<EntityLiving>) worldObj.getEntitiesWithinAABB(ent.getClass(), aabb))
			if (entity.getEntityData().getBoolean("SoulShard"))
				mobCount++;

		return mobCount >= Configs.maxEntities;
	}

	private void spawnEntities(EntityLiving[] ents) {
		for (EntityLiving ent : ents) {
			int counter = 0;
			do {
				counter++;
				if (counter >= Configs.maxspawns) {
					ent.setDead();
					break;
				}
				double x = xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * Configs.spawnrange;
				double y = yCoord + worldObj.rand.nextInt(4) - 1;
				double z = zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * Configs.spawnrange;
				ent.setLocationAndAngles(x, y, z, worldObj.rand.nextFloat() * 360.0F, 0.0F);
			}
			while (!canSpawnAtCoords(ent) || counter >= Configs.maxspawns);

			if (!ent.isDead)
				worldObj.spawnEntityInWorld(ent);
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		shard = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Slot" + 0));

		if (shard != null) {
			tier = Utils.getShardTier(shard);
			entName = Utils.getShardBoundEnt(shard);
			owner = Utils.getShardBoundPlayer(shard);
		}

		if (nbt.hasKey("CustomName", 8))
			this.cageName = nbt.getString("CustomName");

		active = nbt.getBoolean("active");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (shard != null) {
			NBTTagCompound tag = new NBTTagCompound();
			shard.writeToNBT(tag);
			nbt.setTag("Slot" + 0, tag);
		}

		if (this.hasCustomInventoryName())
			nbt.setString("CustomName", this.cageName);

		nbt.setBoolean("active", active);
	}

	public int getSizeInventory() {
		return 7;
	}

	public ItemStack getStackInSlot(int slot) {
		return slot == 0 ? shard : null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (var1 == 0 && shard != null) {
			ItemStack stack = shard;
			shard = null;
			tier = 0;
			entName = null;
			return stack;
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot == 0) {
			shard = stack;
			tier = Utils.getShardTier(shard);
			entName = Utils.getShardBoundEnt(shard);
			owner = Utils.getShardBoundPlayer(shard);
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (shard != null) {
			ItemStack itemstack = shard;
			shard = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.cageName : "container.soulcage";
	}

    @Override
	public boolean hasCustomInventoryName() {
		return this.cageName != null && this.cageName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

	public byte getTier() {
		return tier;
	}

	public String getEntityName() {
		return entName;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 && stack != null && stack.getItem() == ModRegistry.ItemSoulShard && Utils.isShardBound(stack) && Utils.getShardTier(stack) > 0;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
        delay = pkt.func_148857_g().getInteger("delay");
        counter = pkt.func_148857_g().getInteger("counter");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("delay", delay);
        tag.setInteger("counter", counter);
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int face) {
		return new int[]{slot};
	}

    @Override
	public boolean canInsertItem(int slot, ItemStack stack, int size) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int size) {
		return true;
	}
}