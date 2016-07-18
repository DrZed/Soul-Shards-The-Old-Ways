package HxCKDMS.HxCShards.guihandler.containers;

import HxCKDMS.HxCShards.tileentity.TileEntityForge;
import HxCKDMS.HxCShards.utils.ModRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ContainerForge extends Container {
	private TileEntityForge tileFurnace;
    private int lastProgress, lastCookTime,
            lastBurnTime, lastItemBurnTime;

	public ContainerForge(InventoryPlayer inv, TileEntityForge tileEntityFurnace) {
		tileFurnace = tileEntityFurnace;
		addSlotToContainer(new Slot(tileEntityFurnace, 0, 56, 17));
		addSlotToContainer(new Slot(tileEntityFurnace, 1, 56, 53));
		addSlotToContainer(new SlotFurnace(inv.player, tileEntityFurnace, 2, 116, 34));

		int i;
		for (i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (i = 0; i < 9; ++i)
			addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
	}

    @Override
    public void addCraftingToCrafters(ICrafting craft) {
        super.addCraftingToCrafters(craft);
        craft.sendProgressBarUpdate(this, 0, tileFurnace.progress);
        craft.sendProgressBarUpdate(this, 1, tileFurnace.itemCookTime);
        craft.sendProgressBarUpdate(this, 2, tileFurnace.fuelTime2);
        craft.sendProgressBarUpdate(this, 3, tileFurnace.fuelTime);
    }

    @SuppressWarnings("unchecked")
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

        for (ICrafting crafter : (List<ICrafting>) crafters) {
            if (lastProgress != tileFurnace.progress) {
                crafter.sendProgressBarUpdate(this, 0, tileFurnace.progress);
            }

            if (lastCookTime != tileFurnace.itemCookTime) {
                crafter.sendProgressBarUpdate(this, 1, tileFurnace.itemCookTime);
            }

            if (lastBurnTime != tileFurnace.fuelTime2) {
                crafter.sendProgressBarUpdate(this, 2, tileFurnace.fuelTime2);
            }

            if (lastItemBurnTime != tileFurnace.fuelTime) {
                crafter.sendProgressBarUpdate(this, 3, tileFurnace.fuelTime);
            }
        }

        lastProgress = tileFurnace.progress;
		lastBurnTime = tileFurnace.itemCookTime;
		lastCookTime = tileFurnace.fuelTime2;
		lastItemBurnTime = tileFurnace.fuelTime;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int type, int ticks) {
        switch (type) {
            case 0:
                tileFurnace.progress = ticks; break;
            case 1:
                tileFurnace.itemCookTime = ticks; break;
            case 2:
                tileFurnace.fuelTime2 = ticks; break;
            case 3:
                tileFurnace.fuelTime = ticks; break;
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileFurnace.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int islot) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(islot);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (islot == 2) {
				if (!this.mergeItemStack(itemstack1, 4, 39, false))
					return null;
				slot.onSlotChange(itemstack1, itemstack);
			} else if (islot != 1 && islot != 0) {
				if (ModRegistry.sFHandler.getSmeltingResult(itemstack1) != null) {
					if (!mergeItemStack(itemstack1, 0, 1, false))
						return null;
				} else if (TileEntityForge.isItemFuel(itemstack1)) {
					if (!mergeItemStack(itemstack1, 1, 2, false))
						return null;
				} else if (islot >= 4 && islot < 31) {
					if (!mergeItemStack(itemstack1, 31, 39, false))
						return null;
				} else if (islot >= 30 && islot < 39 && !mergeItemStack(itemstack1, 4, 31, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, 4, 39, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0)
				slot.putStack(null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize)
				return null;

			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}
}
