package HxCKDMS.HxCShards.proxies;

import HxCKDMS.HxCShards.renderer.RenderSoulCage;
import HxCKDMS.HxCShards.tileentity.TileEntityCage;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class ClientProxy implements IProxy {
	
	public void load(){
		registerRenderers();
	}
	
	private static void registerRenderers() {
		// Soul Cage
		TileEntitySpecialRenderer cageRender = new RenderSoulCage();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCage.class, cageRender);
	}
}
