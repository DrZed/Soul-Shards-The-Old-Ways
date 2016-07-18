package HxCKDMS.HxCShards.events;

import HxCKDMS.HxCShards.utils.ModRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CreateShardEvent {

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent event) {

		if (event.world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		if (event.entityPlayer.getHeldItem() == null
				|| event.entityPlayer.getHeldItem().getItemDamage() != 0
				|| !event.entityPlayer.isSneaking()) {
			return;
		}

		if (event.world.getBlock(event.x, event.y, event.z) != Blocks.glowstone) {
			return;
		}

		if (checkHorizontal(event.world, event.x, event.y, event.z) || checkVertical(event.world, event.x, event.y, event.z)) {
			if (!event.entityPlayer.capabilities.isCreativeMode) {
				event.entityPlayer.getHeldItem().stackSize--;
			}

			event.world.func_147480_a(event.x, event.y, event.z, false);

			ForgeDirection dir = ForgeDirection.getOrientation(event.face);

			event.world.playSoundEffect(event.x, event.y, event.z, "portal.trigger", 0.1F, 1.0F);

			event.world.spawnEntityInWorld(new EntityItem(event.world,
					event.x + (dir.offsetX * 1.75D), event.y
					+ (dir.offsetY * 1.75D) + 0.5D, event.z
					+ (dir.offsetZ * 1.75D), new ItemStack(
							ModRegistry.ItemSoulShard)));
		}

	}

	private boolean checkHorizontal(World world, int x, int y, int z) {
		ForgeDirection[] VALID_DIRECTIONS = new ForgeDirection[] {
				ForgeDirection.NORTH, ForgeDirection.SOUTH,
				ForgeDirection.EAST, ForgeDirection.WEST };

		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newX = x + dir.offsetX;
			int newZ = z + dir.offsetZ;

			if (world.getBlock(newX, y, newZ) != Blocks.netherrack) {
				return false;
			}
		}

		return true;
	}

	private boolean checkVertical(World world, int x, int y, int z) {
		ForgeDirection[] VALID_DIRECTIONS = new ForgeDirection[] {
				ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST,
				ForgeDirection.WEST };
		boolean isFormed = true;

		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newX = x + dir.offsetX;
			int newY = y + dir.offsetY;

			if (world.getBlock(newX, newY, z) != Blocks.netherrack) {
				isFormed = false;
				break;
			}
		}

		if (isFormed) {
			return true;
		}

		VALID_DIRECTIONS = new ForgeDirection[] { ForgeDirection.UP,
				ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH };

		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newZ = z + dir.offsetZ;
			int newY = y + dir.offsetY;

			if (world.getBlock(x, newY, newZ) != Blocks.netherrack) {
				return false;
			}
		}

		return true;
	}
}
