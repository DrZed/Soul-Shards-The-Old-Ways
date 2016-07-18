package HxCKDMS.HxCShards.tileentity;

import HxCKDMS.HxCShards.utils.ModRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

public class TileEntityForge extends TileEntity implements ISidedInventory {

	private static final int[] slotsTop = new int[]{0};
    private static final int[] slotsSides = new int[]{1};
	private static final int[] slotsBottom = new int[]{2};

	private ItemStack[] furnaceItemStacks = new ItemStack[3];

	public int fuelTime, fuelTime2, progress, itemCookTime;

	@Override
	public int getSizeInventory() {
		return furnaceItemStacks.length;
	}

    public boolean isBurning() {
        return itemCookTime > 0 && fuelTime > 0;
    }

    @Override
	public ItemStack getStackInSlot(int var1) {
		return furnaceItemStacks[var1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int ammount) {
		if (furnaceItemStacks[slot] != null) {
			ItemStack itemstack;
			if (this.furnaceItemStacks[slot].stackSize <= ammount) {
				itemstack = this.furnaceItemStacks[slot];
				furnaceItemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = furnaceItemStacks[slot].splitStack(ammount);
				if (furnaceItemStacks[slot].stackSize == 0)
					furnaceItemStacks[slot] = null;
				return itemstack;
			}
		} else return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (furnaceItemStacks != null) {
			ItemStack itemstack = furnaceItemStacks[slot];
			furnaceItemStacks[slot] = null;
			return itemstack;
		} return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		furnaceItemStacks[slot] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

    @Override
    public String getInventoryName() {
        return StatCollector.translateToLocal("tile.hxcshards.block.forge");
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		furnaceItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tabCompound1 = tagList.getCompoundTagAt(i);
			byte byte0 = tabCompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < furnaceItemStacks.length)
				furnaceItemStacks[byte0] = ItemStack.loadItemStackFromNBT(tabCompound1);
		}
        progress = tagCompound.getShort("Progress");
		itemCookTime = tagCompound.getShort("CookTime");
        fuelTime = tagCompound.getShort("Fuel");
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("Progress", progress);
        tagCompound.setInteger("CookTime", itemCookTime);
        tagCompound.setInteger("Fuel", fuelTime);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < furnaceItemStacks.length; ++i) {
			if (furnaceItemStacks[i] != null) {
				NBTTagCompound tagCompound1 = new NBTTagCompound();
				tagCompound1.setByte("Slot", (byte) i);
				furnaceItemStacks[i].writeToNBT(tagCompound1);
				tagList.appendTag(tagCompound1);
			}
		}
		tagCompound.setTag("Items", tagList);
	}

    public boolean checkFuel() {
        if (fuelTime > 0) {
            fuelTime--;
        } else if (fuelTime == 0 && itemCookTime > 0 && progress < itemCookTime-1) {
            fuelTime = ModRegistry.sFHandler.getFuelBurnTime(getStackInSlot(1));
            decrStackSize(1, 1);
            if (fuelTime == 0) {
                progress = 0;
                itemCookTime = 0;
                return false;
            } else fuelTime2 = fuelTime;
        }
        return true;
    }

    private boolean cookItem() {
        if (getStackInSlot(2) != null && ModRegistry.sFHandler.getSmeltingResult(getStackInSlot(0)).getItem() == getStackInSlot(2).getItem()) {
            ItemStack out = ModRegistry.sFHandler.getSmeltingResult(getStackInSlot(0));
            if (out.stackSize + out.stackSize > 64)
                return false;
            out.stackSize += out.stackSize;
            setInventorySlotContents(2, out);
        } else {
            setInventorySlotContents(2, ModRegistry.sFHandler.getSmeltingResult(getStackInSlot(0)));
        }
        decrStackSize(0, 1);
        return true;
    }

	public void updateEntity() {
        if (progress < 0) {progress = 0;}

        if (!checkFuel()) return;

        if (getStackInSlot(0) == null || (getStackInSlot(2) != null && getStackInSlot(2).stackSize == 64)) return;

        if (itemCookTime > 0 && progress != itemCookTime && itemCookTime != ModRegistry.sFHandler.getSmeltingTime(getStackInSlot(0))) {
            float f = (float)progress/(float)itemCookTime;
            itemCookTime = ModRegistry.sFHandler.getSmeltingTime(getStackInSlot(0));
            progress = Math.round(f * itemCookTime);
        }

        if (itemCookTime > 0 && progress >= itemCookTime) {
            if (!cookItem()) return;
            progress = 0;
            itemCookTime = 0;
        } else if (itemCookTime > 0) {
            progress++;
        } else {
            itemCookTime = ModRegistry.sFHandler.getSmeltingTime(getStackInSlot(0));
            progress = 0;
        }
	}

	public static boolean isItemFuel(ItemStack itemstack) {
        return itemstack != null && ModRegistry.sFHandler.getFuelList().containsKey(itemstack);
    }

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return ((slot == 1 && isItemFuel(itemstack)) ||
                (slot == 0 && ModRegistry.sFHandler.getSmeltingResult(itemstack) != null) || slot == 2);
	}

    @Override
	public int[] getAccessibleSlotsFromSide(int face) {
		return face == 0 ? slotsBottom : (face == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int size) {
		return isItemValidForSlot(slot, itemstack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int size) {
		return size != 0 || slot != 1 || itemstack.getItem() == Items.bucket;
	}
}