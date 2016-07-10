package HxCKDMS.HxCShards.guihandler;

import HxCKDMS.HxCShards.guihandler.containers.ContainerForge;
import HxCKDMS.HxCShards.tileentity.TileEntityForge;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if(tileEnt instanceof TileEntityForge) {
			return new ContainerForge(player.inventory, (TileEntityForge)tileEnt);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if(tileEnt instanceof TileEntityForge) {
			return new GuiSoulForge(player.inventory, (TileEntityForge)tileEnt);
		}
		return null;
	}

}