package HxCKDMS.HxCShards.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import HxCKDMS.HxCShards.item.ItemSoulShard;
import HxCKDMS.HxCShards.tileentity.TileEntityCage;
import HxCKDMS.HxCShards.utils.Utils;

public class RenderSoulCage extends TileEntitySpecialRenderer {
	private Minecraft mc;
	private TileEntityCage TECage;

	public RenderSoulCage() {
		this.mc = Minecraft.getMinecraft();
	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
        TECage = (TileEntityCage) entity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		getEntityForRender(((IInventory)entity).getStackInSlot(0), x, y, z);
		GL11.glPopMatrix();
	}

	private void getEntityForRender(ItemStack stack, double x, double y, double z) {
		if(stack != null) {
			if(stack.getItem() instanceof ItemSoulShard) {
                Entity mob = EntityList.createEntityByName(Utils.getShardBoundEnt(stack), mc.theWorld);
				float f1 = 0.4375F;
				GL11.glTranslatef(0.0F, 0.4F, 0.0F);
				GL11.glRotatef(20F, 0F, 0F, 1F);
                if(TECage.getBlockMetadata() == 1)
					GL11.glRotatef(Minecraft.getSystemTime() / -10, 0F, 1F, 0F);
				GL11.glRotatef(-20F, 1F, 0F, 0F);
				GL11.glTranslatef(0.0F, -0.4F, 0.0F);
				GL11.glScalef(f1, f1, f1);
				mob.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
				RenderManager.instance.renderEntityWithPosYaw(mob, 0, 0, 0, 0F, 1F);
			}
		}
	}
}
