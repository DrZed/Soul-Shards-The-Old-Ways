package HxCKDMS.HxCShards.item.tools;

import HxCKDMS.HxCShards.utils.Configs;
import HxCKDMS.HxCShards.utils.ModRegistry;
import HxCKDMS.HxCShards.utils.Reference;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemAxeSoul extends ItemAxe {
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon2;
	@SideOnly(Side.CLIENT)
	private IIcon itemIcon3;

	public ItemAxeSoul(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(ModRegistry.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemAxeSoul)
			return Reference.modName + ".soultool.axe";
		if (stack.getItem() == ModRegistry.ItemAxeSoul2)
			return Reference.modName + ".soultool2.axe";
		if (stack.getItem() == ModRegistry.ItemAxeSoul3)
			return Reference.modName + ".soultool3.axe";
		return Reference.modName + ".soultool.axe";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":tools/axeSoul" + (Configs.useSSTOWTextures ? "_old" : ""));
		itemIcon2 = iconRegister.registerIcon(Reference.modID + ":tools/axeImprovedSoul");
		itemIcon3 = iconRegister.registerIcon(Reference.modID + ":tools/axeDraconicSoul");
	}

	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if (stack.getItem() == ModRegistry.ItemAxeSoul)
			return itemIcon;
		if (stack.getItem() == ModRegistry.ItemAxeSoul2)
			return itemIcon2;
		if (stack.getItem() == ModRegistry.ItemAxeSoul3)
			return itemIcon3;
		return itemIcon;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
}
