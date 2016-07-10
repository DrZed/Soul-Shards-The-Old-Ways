package HxCKDMS.HxCShards.guihandler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import HxCKDMS.HxCShards.guihandler.containers.ContainerForge;
import HxCKDMS.HxCShards.tileentity.TileEntityForge;
import HxCKDMS.HxCShards.utils.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSoulForge extends GuiContainer {

	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(Reference.modID + ":textures/gui/container/forge.png");
	private TileEntityForge tileFurnace;

	public GuiSoulForge(InventoryPlayer invPlayer, TileEntityForge tile) {
		super(new ContainerForge(invPlayer, tile));
		tileFurnace = tile;

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String string = tileFurnace.isCustomInventoryName() ? tileFurnace.getInventoryName() :
                I18n.format(tileFurnace.getInventoryName());
		fontRendererObj.drawString(string, xSize / 2 - fontRendererObj.getStringWidth(string), 6, 4210752);
		fontRendererObj.drawString( I18n.format("container.inventory"), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (tileFurnace.isBurning()) {
            float f = ((float)tileFurnace.fuelTime/(float)tileFurnace.fuelTime2);
			drawTexturedModalRect(k + 56, Math.round(l + 36 + 12 - (f*12)), 176, Math.round(12 - (f*12)), 14, Math.round((f*12) + 2));
		}


        float f = ((float)tileFurnace.progress/(float)tileFurnace.itemCookTime);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, Math.round(24*f) + 1, 16);
	}
}
